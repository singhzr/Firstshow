package com.example.BookMyShow.Entities;

import com.example.BookMyShow.Enums.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "tickets")
public class Ticket {//1.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    private String seatNumbersBooked;

    private int foodAttached;

    private Integer totalAmountPaid;

    @JsonIgnore
    @JoinColumn
    @ManyToOne
    private Show show;

    @JsonIgnore
    @JoinColumn
    @ManyToOne
    private User user;
}
//1. In this class we are not adding movieName attribute because from
//   ticket entity we can get show entity and from show entity we can get movie entity