package com.example.BookMyShow.Entities;

import com.example.BookMyShow.Enums.Genre;
import com.example.BookMyShow.Enums.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder //creates object of this class
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "movie_info")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(unique = true, nullable = false)
    private String movieName;

    @Enumerated(value = EnumType.STRING)
    private Genre genre;

    @Enumerated(value = EnumType.STRING)
    private Language movieLanguage;

    private double rating;

    private LocalDate releaseDate;

    private double duration;

    private String movieImageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> showList = new ArrayList<>();
}
