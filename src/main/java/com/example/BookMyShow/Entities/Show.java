package com.example.BookMyShow.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer showId;

    private LocalDate showDate;

    @Column(name = "show_time", columnDefinition = "TIME") //The columnDefinition attribute explicitly tells JPA to use the SQL TIME type for the database column.
    private LocalTime showTime;
    //TIME (SQL): Second (or millisecond) precision, depending on the database.
    //LocalTime (Java): Nanosecond precision.

    //1 second = 1,000 milliseconds (ms)
    //1 second = 10 lakh microseconds (µs)
    //1 second = 100 crore nanoseconds (ns)

    @JsonIgnore
    @JoinColumn
    @ManyToOne
    private Movie movie;

    @JsonIgnore
    @JoinColumn
    @ManyToOne
    private Theater theater;

    @JsonIgnore
    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<ShowSeat> showSeatList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<Ticket> ticketList = new ArrayList<>();
    public Show(LocalDate showDate, LocalTime showTime) {
        this.showDate = showDate;
        this.showTime = showTime;
    }
}
