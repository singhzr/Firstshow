package com.example.BookMyShow.Transformers;

import com.example.BookMyShow.Entities.Movie;
import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.RequestDTOs.AddMovieRequest;
import com.example.BookMyShow.RequestDTOs.AddShowRequest;

public class ShowTransformer {

    public static Show convertShowRequestToEntity(AddShowRequest showRequest){

        Show show = Show.builder()
                .showDate(showRequest.getShowDate())
                .showTime(showRequest.getShowTime())
                .build();

        return show;
    }
}
