package com.example.BookMyShow.Entities;

import com.example.BookMyShow.Enums.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "theater_seats")
public class TheaterSeat { //Represents all the theater seats in that particular theater

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer theaterSeatId;

    private String seatNo;

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;

    @JsonIgnore
    @JoinColumn
    @ManyToOne
    private Theater theater;
}
