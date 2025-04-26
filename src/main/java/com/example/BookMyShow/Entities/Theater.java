package com.example.BookMyShow.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "theaters")
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer theaterId;

    private String name;

    private String address;

    private Integer noOfScreens;

    @JsonIgnore
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<TheaterSeat> theaterSeatList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<Show> showList = new ArrayList<>();
}
