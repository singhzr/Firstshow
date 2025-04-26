package com.example.BookMyShow.Services;

import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.Entities.ShowSeat;
import com.example.BookMyShow.Entities.Ticket;
import com.example.BookMyShow.Entities.User;
import com.example.BookMyShow.Enums.SeatStatus;
import com.example.BookMyShow.Repositories.ShowRepository;
import com.example.BookMyShow.Repositories.ShowSeatRepository;
import com.example.BookMyShow.Repositories.TicketRepository;
import com.example.BookMyShow.Repositories.UserRepository;
import com.example.BookMyShow.RequestDTOs.AddTicketRequest;
import com.example.BookMyShow.Response.ShowTicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Transactional
    public String bookTicket(AddTicketRequest bookTicketRequest, String sessionId) throws Exception {

        try{
            Optional<Show> showOptional = showRepository.findById(bookTicketRequest.getShowId());
            if (showOptional.isEmpty()) {
                throw new Exception("Show with show id " + bookTicketRequest.getShowId() + " does not exists");
            }
            Show show = showOptional.get();

            //Check for seat available or not
            List<ShowSeat> showSeatList = ticketRepository.findSeatsForUpdate(bookTicketRequest.getShowId(),
                    bookTicketRequest.getSeatList());

            int totalBookingAmount = 0;

            for (ShowSeat showSeat : showSeatList) {

                if (showSeat.getStatus() != SeatStatus.LOCKED) {
                    return "Seat is not locked";
                }

                if (!sessionId.equals(showSeat.getLockedBy())) {
                    return "Seat locked by another user";
                }
                if (showSeat.getLockTime().isBefore(LocalDateTime.now().minusMinutes(5))) {
                    return "Lock expired";
                }

                if (showSeat.getStatus() == SeatStatus.BOOKED) {
                    throw new Exception("Seat No " + showSeat.getSeatNo() + " is already booked.");
                }
            }

            //If we reach here that means all the seats were available

            for(ShowSeat showSeat : showSeatList){
                showSeat.setStatus(SeatStatus.BOOKED);
                showSeat.setLockedBy(null);
                showSeat.setLockTime(null);
                totalBookingAmount = totalBookingAmount + showSeat.getPrice();
                showSeatRepository.save(showSeat);
            }

            User user = userRepository.findByEmailId(bookTicketRequest.getEmailId());

            Ticket ticket = Ticket.builder()
                    .seatNumbersBooked(bookTicketRequest.getSeatList().toString())
                    .totalAmountPaid(totalBookingAmount)
                    .show(show) //FK being set
                    .user(user) //FK being set
                    .foodAttached(bookTicketRequest.getFoodAttached())
                    .build();

            user.getTicketList().add(ticket); //Bidirectional mapping
            show.getTicketList().add(ticket); //Bidirectional mapping

            ticket = ticketRepository.save(ticket);

            return ticket.getTicketId().toString();
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
    public ShowTicketResponse viewTicket(Integer ticketId)throws Exception{

        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        if(optionalTicket.isEmpty()){
            throw new Exception("Ticket does not exists.");
        }

        Ticket ticket = optionalTicket.get();

        String emailId = ticket.getUser().getEmailId();

        Show show = ticket.getShow();
        String movieName = show.getMovie().getMovieName();
        String movieLanguage = String.valueOf(show.getMovie().getMovieLanguage());
        String movieImageURL = show.getMovie().getMovieImageUrl();
        String theaterInfo = show.getTheater().getName()+" "+show.getTheater().getAddress();
        String bookedSeats = ticket.getSeatNumbersBooked();

        ShowTicketResponse showTicketResponse = ShowTicketResponse.builder()
                .movieName(movieName)
                .movieImageURL(movieImageURL)
                .theaterInfo(theaterInfo)
                .showDate(show.getShowDate())
                .showTime(show.getShowTime())
                .seatNos(bookedSeats)
                .emailId(emailId)
                .ticketId(ticket.getTicketId())
                .movieLanguage(movieLanguage)
                .totalAmt(ticket.getTotalAmountPaid())
                .foodAttached(ticket.getFoodAttached())
                .build();

//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//
//        simpleMailMessage.setFrom("springacciojob@gmail.com");
//        simpleMailMessage.setTo(emailId);
//        simpleMailMessage.setSubject("Movie Ticket Confirmation");
//        simpleMailMessage.setText(  "Movie name - "+showTicketResponse.getMovieName()+"\n"+
//                                    "Show time - "+showTicketResponse.getShowTime()+"\n"+
//                                    "Show date - "+showTicketResponse.getShowDate()+"\n"+
//                                    "Seats booked - "+bookedSeats+"\n"+
//                                    "Food attached - "+showTicketResponse.getFoodAttached()+"\n"+
//                                    "Theater - "+showTicketResponse.getTheaterInfo()+"\n"+
//                                    "Amount paid - "+showTicketResponse.getTotalAmt()+"\n");
//        mailSender.send(simpleMailMessage);

        return showTicketResponse;
    }
    public String cancelTicket(Integer ticketId)throws Exception{

        Ticket ticketEntity = ticketRepository.findTicket(ticketId);

        if(ticketEntity == null){
            throw new Exception("Ticket with "+ticketId+" does not exists in the database.");
        }
        String temp = ticketEntity.getSeatNumbersBooked();
        String ticketsBooked = temp.substring(1,temp.length()-1);

        String[] seatList = ticketsBooked.split(", ");

        List<ShowSeat> showSeatList = showSeatRepository.findAll();

        for(String seat : seatList){
            for (ShowSeat showSeat : showSeatList){

                if(showSeat.getSeatNo().equals(seat)){

                    showSeat.setStatus(SeatStatus.AVAILABLE);
                    showSeat.setFoodAttached(0);

                    showSeatRepository.save(showSeat);
                }
            }
        }
        ticketRepository.deleteTicketById(ticketId);
        return "Ticket has been cancelled and refund of Rs."+ticketEntity.getTotalAmountPaid()+" has been processed.";
    }

    public List<ShowTicketResponse> viewAllTicketOfUser(String username) throws Exception {

        User user = userRepository.findByUserName(username);

        List<Ticket> allTicketOfTheUser = ticketRepository.allTicketsOfTheUser(user.getUserId());

        List<ShowTicketResponse> showTicketResponseList = new ArrayList<>();

        for(Ticket ticket : allTicketOfTheUser){

            showTicketResponseList.add(viewTicket(ticket.getTicketId()));
        }
        return showTicketResponseList;
    }

}