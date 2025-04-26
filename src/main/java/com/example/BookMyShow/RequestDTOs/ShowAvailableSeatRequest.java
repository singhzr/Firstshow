package com.example.BookMyShow.RequestDTOs;

import com.example.BookMyShow.Enums.SeatType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowAvailableSeatRequest {
    private Integer showId;
    private SeatType seatType;
}
