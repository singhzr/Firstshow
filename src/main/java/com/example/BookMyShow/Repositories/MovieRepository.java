package com.example.BookMyShow.Repositories;

import com.example.BookMyShow.Entities.Movie;
import com.example.BookMyShow.Enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //2.
public interface MovieRepository extends JpaRepository<Movie,Integer> {

    Movie findMovieByMovieNameAndAndMovieLanguage(String movieName, Language language);

    Movie findMovieByMovieName(String movieName);

    Movie findMovieByMovieId(Integer movieId);


    List<Movie> findAllByDurationGreaterThanEqual(double duration);


}
//1. How we can interact with JpaRepository there are 3 ways :
//   a) Just call the inbuilt methods
//   b) Just define the method. No query nothing but naming of the method has to be strict
//   c) Write the actual queries

//2. If we don't write @Repository it will then also work because it is interface we don't need to create object
//   of this. The class which implements the methods written in JpaRepository already has @Repository written on it