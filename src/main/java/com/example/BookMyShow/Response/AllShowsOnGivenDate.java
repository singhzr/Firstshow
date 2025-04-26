package com.example.BookMyShow.Response;

import com.example.BookMyShow.Entities.Show;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
public class AllShowsOnGivenDate {

    private String theaterName;
    private List<Show> showsList = new ArrayList<>();
}
