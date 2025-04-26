package com.example.BookMyShow.RequestDTOs;

import com.example.BookMyShow.Enums.Genre;
import com.example.BookMyShow.Enums.Language;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class AddMovieRequest {

    private String movieName;

    private Genre genre;

    private Language movieLanguage;

    private LocalDate releaseDate;
    private double rating;
    private double duration;
    private String url;
}
