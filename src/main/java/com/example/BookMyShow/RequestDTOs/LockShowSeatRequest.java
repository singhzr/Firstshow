package com.example.BookMyShow.RequestDTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LockShowSeatRequest {
    private int showId;
    public List<String> seatList;
}
