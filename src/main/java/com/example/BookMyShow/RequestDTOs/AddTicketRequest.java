package com.example.BookMyShow.RequestDTOs;

import com.example.BookMyShow.Enums.SeatType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddTicketRequest {

    private int showId;

    public List<String> seatList;

    private int foodAttached;

    private String emailId;
}
