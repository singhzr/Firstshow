package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.Entities.Movie;
import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.RequestDTOs.AddMovieRequest;
import com.example.BookMyShow.RequestDTOs.GetShowsOfMovieOnGivenDate;
import com.example.BookMyShow.Response.AllShowsOnGivenDate;
import com.example.BookMyShow.Services.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/addMovie")
    public ResponseEntity addMovie(@RequestBody AddMovieRequest addMovieRequest) {

        String result = movieService.addMovie(addMovieRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/deleteMovieByMovieId")
    public ResponseEntity deleteMovieByMovieId(@RequestParam("movieId") Integer movieId) {

        try {
            String result = movieService.deleteMovieByMovieId(movieId);
            return new ResponseEntity(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/totalCollectionOfaMovie")
    public ResponseEntity totalCollectionOfaMovie(@RequestParam("movieName") String movieName) {

        try {
            String result = movieService.totalCollectionOfaMovie(movieName);
            return new ResponseEntity(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/showsoftheMovieOnaGivenDate")
    public ResponseEntity showsoftheMovieOnaGivenDate(GetShowsOfMovieOnGivenDate getShowsOfMovieOnGivenDate) {

        try {
            String result = movieService.showsoftheMovieOnaGivenDate(getShowsOfMovieOnGivenDate);
            return new ResponseEntity(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/allListedMovies")
    public ResponseEntity allListedMovies() {
        List<Movie> movieList = movieService.allListedMovies();
        return new ResponseEntity(movieList, HttpStatus.OK);
    }

    @GetMapping("/movieData/{movieId}")
    public ResponseEntity movieData(@PathVariable("movieId") Integer movieId) {
        Movie movie = movieService.movieData(movieId);
        return new ResponseEntity(movie, HttpStatus.OK);
    }

    @GetMapping("/allShowsoftheMovie/{movieId}/{city}")
    public ResponseEntity allShowsoftheMovie(@PathVariable("movieId") Integer movieId,
                                             @PathVariable("city") String city) {

        try {
            HashMap<String, List<Show>> result = movieService.allShowsoftheMovie(movieId, city);
            return new ResponseEntity(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listedMoviesCityWise/{city}")
    public ResponseEntity listedMoviesCityWise(@PathVariable("city") String city) {
        try {
            List<Movie> movieList = movieService.listedMoviesCityWise(city);

            return new ResponseEntity(movieList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}