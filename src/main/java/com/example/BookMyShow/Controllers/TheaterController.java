package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.RequestDTOs.AddTheaterRequest;
import com.example.BookMyShow.RequestDTOs.AddTheaterSeatsRequest;
import com.example.BookMyShow.Services.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping("/addTheater")
    public ResponseEntity addTheater(@RequestBody AddTheaterRequest addTheaterRequest){

        String result = theaterService.addTheater(addTheaterRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addTheaterSeats")
    public ResponseEntity addTheaterSeats(@RequestBody AddTheaterSeatsRequest addTheaterSeatsRequest){
        String result = theaterService.addTheaterSeats(addTheaterSeatsRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/allShowsInParticularTheater")
    public ResponseEntity allShowsInParticularTheater(@RequestParam("theaterId") Integer theaterId){

        try {
            String result = theaterService.allShowsInParticularTheater(theaterId);
            return new ResponseEntity(result, HttpStatus.OK);

        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


