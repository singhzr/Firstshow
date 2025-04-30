package com.example.BookMyShow.RequestDTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddTicketRequest {

    private Integer showId;

    public List<String> seatList;

    private Integer foodAttached;

    private String emailId;
}
