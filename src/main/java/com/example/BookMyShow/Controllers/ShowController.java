package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.Entities.ShowSeat;
import com.example.BookMyShow.Entities.Theater;
import com.example.BookMyShow.Entities.TheaterSeat;
import com.example.BookMyShow.RequestDTOs.AddShowRequest;
import com.example.BookMyShow.RequestDTOs.AddShowSeatsRequest;
import com.example.BookMyShow.RequestDTOs.ShowAvailableSeatRequest;
import com.example.BookMyShow.Services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping("/addShow")
    public ResponseEntity addShow(@RequestBody AddShowRequest addShowRequest){

        try {
            String result = showService.addShow(addShowRequest);
            return new ResponseEntity(result, HttpStatus.OK);

        }catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/addShowSeats")
    public ResponseEntity addShowSeats(@RequestBody AddShowSeatsRequest addShowSeatsRequest){
        try {
            String result = showService.addShowSeats(addShowSeatsRequest);
            return new ResponseEntity(result, HttpStatus.OK);

        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/showAvailableSeats")
    public ResponseEntity showAvailableSeats(@RequestBody ShowAvailableSeatRequest showAvailableSeatRequest) {

        try {
            List<String> result = showService.showAvailableSeats(showAvailableSeatRequest);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/deleteShowByShowId")
    public ResponseEntity deleteShowByShowId(@RequestParam("showId") Integer showId){

        try {
            String result = showService.deleteShowByShowId(showId);
            return new ResponseEntity(result, HttpStatus.OK);

        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/theaterDetails/{showId}")
    public ResponseEntity theaterDetails(@PathVariable("showId") Integer showId){

        try {
            List<TheaterSeat> theaterSeatList  = showService.theaterDetails(showId);
            return new ResponseEntity(theaterSeatList, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/showSeatsData/{showId}")
    public ResponseEntity showSeatsData(@PathVariable("showId") Integer showId){

        try {
            List<ShowSeat> showSeatData  = showService.showSeatData(showId);

            return new ResponseEntity(showSeatData, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
