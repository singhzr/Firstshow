package com.example.BookMyShow.Services;

import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.Entities.Theater;
import com.example.BookMyShow.Entities.TheaterSeat;
import com.example.BookMyShow.Enums.SeatType;
import com.example.BookMyShow.Repositories.ShowRepository;
import com.example.BookMyShow.Repositories.TheaterRepository;
import com.example.BookMyShow.RequestDTOs.AddTheaterRequest;
import com.example.BookMyShow.RequestDTOs.AddTheaterSeatsRequest;
import com.example.BookMyShow.Transformers.TheaterTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private ShowRepository showRepository;

    public String addTheater(AddTheaterRequest theaterRequest){

        Theater theater = TheaterTransformer.convertTheaterRequestToEntity(theaterRequest);

        theater = theaterRepository.save(theater);
        return "Theater has been saved to DB with theaterId "+theater.getTheaterId();
    }


    public String addTheaterSeats(AddTheaterSeatsRequest theaterSeatsRequest){
        int classicSeats = theaterSeatsRequest.getNoOfClassicSeats();
        int premiumSeats = theaterSeatsRequest.getNoOfPremiumSeats();

        Theater theater = theaterRepository.findById(theaterSeatsRequest.getTheaterId()).get();

        List<TheaterSeat> theaterSeatList = new ArrayList<>();
        int totalSeats = premiumSeats + classicSeats;
        int columns = (int) Math.ceil(Math.sqrt(totalSeats));
        int premiumRows = (int) Math.ceil((double) premiumSeats / columns);
        int classicRows = (int) Math.ceil((double) classicSeats / columns);

        char rowLabel = 'A';
        int seatCounter = 1;


        for (int i = 0; i < premiumRows; i++) {
            int seatsInRow = Math.min(columns, premiumSeats - seatCounter + 1);

            for (int j = 1; j <= seatsInRow; j++) {
                TheaterSeat theaterSeatEntity = TheaterSeat.builder()
                        .seatNo(rowLabel + Integer.toString(j) + "(P)")
                        .seatType(SeatType.PREMIUM)
                        .theater(theater)
                        .build();

                theaterSeatList.add(theaterSeatEntity);
                seatCounter++;
            }
            rowLabel++; // Move to next row
        }

        seatCounter = 1;

        for (int i = 0; i < classicRows; i++) {
            int seatsInRow = Math.min(columns, classicSeats - seatCounter + 1);

            for (int j = 1; j <= seatsInRow; j++) {
                TheaterSeat theaterSeatEntity = TheaterSeat.builder()
                        .seatNo(rowLabel + Integer.toString(j) + "(C)")
                        .seatType(SeatType.CLASSIC)
                        .theater(theater)
                        .build();

                theaterSeatList.add(theaterSeatEntity);
                seatCounter++;
            }
            rowLabel++; // Move to next row
        }


        //Setting the variable of the Bidirectional mapping in the parent class
        theater.setTheaterSeatList(theaterSeatList);

        //Save both parent and child : Not required , only save the parent :
        theaterRepository.save(theater);

        return "Theater seats have been added";
    }

    public String allShowsInParticularTheater(Integer theaterId)throws Exception{

        Theater theater = theaterRepository.searchForTheater(theaterId);

        if(theater == null){
            throw new Exception("Theater does not exist.");
        }
        List<Show> showList = showRepository.findAllShows();

        List<Show> showsInTheater = new ArrayList<>();

        for(Show show : showList){

            if(show.getTheater().getTheaterId() == theaterId){
                showsInTheater.add(show);
            }
        }
        String shows = "";
        for(Show show : showsInTheater){

            String movieName = show.getMovie().getMovieName();
            String date = show.getShowDate().toString();
            String time = show.getShowTime().toString();
            shows += movieName+" "+date+" "+time +"\n";
        }
        return shows;
    }
}
