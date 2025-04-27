package com.example.BookMyShow.Entities;

import com.example.BookMyShow.Enums.SeatStatus;
import com.example.BookMyShow.Enums.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "show_seats")
public class ShowSeat {//Represents particular Seat booked for a particular show

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer showSeatId;

    private int price;

    private int foodAttached;

    private String seatNo;// These values will come from the theater

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;// seats based on mapping or seat structure

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Column(name = "lock_time", columnDefinition = "DATETIME")
    private LocalDateTime lockTime;

    private String lockedBy;

    @JsonIgnore
    @JoinColumn
    @ManyToOne
    private Show show;

}
