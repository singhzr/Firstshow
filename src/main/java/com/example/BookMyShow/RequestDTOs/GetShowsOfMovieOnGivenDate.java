package com.example.BookMyShow.RequestDTOs;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetShowsOfMovieOnGivenDate {

    private String movieName;
    private LocalDate localDate;
}
