package com.example.BookMyShow.Response;

import com.example.BookMyShow.Enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowTicketResponse {

    private String movieName;
    private String theaterInfo;
    private LocalDate showDate;
    private LocalTime showTime;
    private int totalAmt;
    private String seatNos;
    private String emailId;
    private int foodAttached;
    private String movieImageURL;
    private String movieLanguage;
    private Integer ticketId;

}
