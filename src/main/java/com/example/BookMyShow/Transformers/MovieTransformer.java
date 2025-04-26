package com.example.BookMyShow.Transformers;

import com.example.BookMyShow.Entities.Movie;
import com.example.BookMyShow.RequestDTOs.AddMovieRequest;

public class MovieTransformer {

    public static Movie convertTheaterRequestToEntity(AddMovieRequest movieRequest){

        Movie movie = Movie.builder()
                .movieLanguage(movieRequest.getMovieLanguage())
                .movieName(movieRequest.getMovieName())
                .duration(movieRequest.getDuration())
                .genre(movieRequest.getGenre())
                .releaseDate(movieRequest.getReleaseDate())
                .rating(movieRequest.getRating())
                .build();

        return movie;
    }
}
