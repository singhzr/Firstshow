package com.example.BookMyShow.RequestDTOs;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class GetShowsOfMovieOnGivenDate {

    private String movieName;
    private LocalDate localDate;
}
