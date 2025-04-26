package com.example.BookMyShow.Transformers;

import com.example.BookMyShow.Entities.Movie;
import com.example.BookMyShow.Entities.Theater;
import com.example.BookMyShow.RequestDTOs.AddMovieRequest;
import com.example.BookMyShow.RequestDTOs.AddTheaterRequest;

public class TheaterTransformer {

    public static Theater convertTheaterRequestToEntity(AddTheaterRequest TheaterRequest){

        Theater theater = Theater.builder()
                .name(TheaterRequest.getName())
                .address(TheaterRequest.getAddress())
                .noOfScreens(TheaterRequest.getNoOfScreens())
                .build();

        return theater;
    }
}
