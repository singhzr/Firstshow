package com.example.BookMyShow.RequestDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnLockShowSeatRequest {
    private int showId;
    private String seatNo;
}
