package com.example.BookMyShow.RequestDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AddShowRequest {
    private LocalDate showDate;

    private LocalTime showTime;

    private String movieName; //From this we will figure out movieEntity

    private Integer theaterId;
}
