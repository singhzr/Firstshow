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
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
    public Integer bookTicket(AddTicketRequest bookTicketRequest, String sessionId) throws Exception {

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
                    return -1; //"Seat is not locked";
                }

                if (!sessionId.equals(showSeat.getLockedBy())) {
                    return -2; //"Seat locked by another user";
                }
                if (showSeat.getLockTime().isBefore(LocalDateTime.now().minusMinutes(5))) {
                    return -3; //"Lock expired";
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

            return ticket.getTicketId();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

        return showTicketResponse;
    }

    public void sendImageEmail(String toEmail, String subject, String base64Image)
            throws MessagingException, IOException {

        String base64Data;
        if (base64Image.contains(",")) {
            base64Data = base64Image.split(",")[1];
        }
        else {
            base64Data = base64Image;
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText("Your movie ticket is attached as an image.", false);

        ByteArrayDataSource dataSource = new ByteArrayDataSource(imageBytes, "image/png");
        helper.addAttachment("ticket.png", dataSource);

        mailSender.send(message);
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


    /*

--->if (base64Image.contains(",")) {
        base64Data = base64Image.split(",")[1];
    }
    This line is checking whether the input base64Image contains a comma, which typically indicates that it's a
    data URI (e.g., data:image/png;base64,iVBORw0KGgo...). If so, it splits the string at the comma and uses
    only the part after the comma, which is the actual Base64-encoded image data.


--->byte[] imageBytes = Base64.getDecoder().decode(base64Data);

    Base64.getDecoder() gives you a decoder that understands Base64 encoding.

    .decode(base64Data) converts the encoded text into the original binary format (in this case, the image's byte content).

    imageBytes can then be used to attach the image, save it to disk, etc.



--->MimeMessage message = mailSender.createMimeMessage();

    mailSender is likely an instance of JavaMailSender (usually injected via Spring).

    createMimeMessage() gives you a fresh MimeMessage object, ready to be configured
    (recipient, subject, body, attachments, etc.).

    This MimeMessage is used by Spring’s MimeMessageHelper to build complex emails (like those with
    attachments or HTML content).

--->MimeMessageHelper helper = new MimeMessageHelper(message, true);

    This line creates a helper object that makes it easier to configure complex email features like attachments,
    HTML content, and inline resources.

    message: The MimeMessage you're working with.
    true: This enables multipart mode, which is required for attachments or multiple content parts (like text + HTML + attachments).
    Without multipart mode, an email can only have a single body part (like plain text or HTML, but not both,
    and definitely no attachments).


    Why use MimeMessageHelper?
    Working directly with MimeMessage is low-level and verbose. MimeMessageHelper simplifies it, providing easy methods like:

    setTo(...) – set recipient

    setSubject(...) – set email subject

    setText(...) – set body (plain text or HTML)

    addAttachment(...) – attach files

    addInline(...) – embed images directly in HTML


---->ByteArrayDataSource dataSource = new ByteArrayDataSource(imageBytes, "image/png");

     ByteArrayDataSource is necessary to give JavaMail the proper context about your image attachment.

     It wraps the byte[] (your image) and tells JavaMail how to handle it by specifying the MIME type ("image/png").

     Why use ByteArrayDataSource?
     The ByteArrayDataSource is used to wrap your image data (byte array) and specify the MIME type
     (in your case, "image/png"), which is required for attaching a file to an email.

     Here's why it's necessary:

     1. Representation of the Image Data:
         ->Your image data is in a byte array format (byte[] imageBytes), which is the raw binary data of the image.

         ->JavaMail (which is the underlying library used for sending emails) does not know how to handle just the byte[]
         directly as an attachment without the MIME type and content information.

    2. MIME Type Specification:
    The MIME type ("image/png" in your case) tells the email client (e.g., Gmail, Outlook) what kind of
    file it is. This ensures that the email client knows how to properly display or handle the attachment.

        ->"image/png" is the MIME type for PNG images.

        ->If the image were in JPEG format, the MIME type would be "image/jpeg", and so on.

    3. DataSource Requirement:
    JavaMail requires a DataSource to represent attachments. ByteArrayDataSource is one of the
    classes that implements the DataSource interface, which is how JavaMail handles attachments and inline resources.


---->What is a DataSource?
    A DataSource is an abstraction that allows JavaMail to handle various types of data
     (e.g., byte arrays, files, streams) as attachments or embedded resources in a message.

    The DataSource interface provides a way to read data (the content) and get its content type (the MIME type)
    without worrying about the underlying format.

    Why is it necessary?
    1.Separation of Content and Transport:

    ->JavaMail is designed to send messages that may contain different types of data
    (e.g., plain text, HTML, images, PDFs). The DataSource helps separate the content from the transport mechanism (email).

    ->By using a DataSource, JavaMail can focus on how to send the data, while DataSource handles what the data is.

    2.MIME Standard Compliance:

    ->MIME (Multipurpose Internet Mail Extensions) is the standard for representing different types of content in
    emails (e.g., text, images, documents).

    ->Emails with attachments or rich content are encoded in MIME format.

    ->The DataSource allows JavaMail to work with the MIME format, as it helps identify the type of content
    (through MIME type) and how to read it.

 */