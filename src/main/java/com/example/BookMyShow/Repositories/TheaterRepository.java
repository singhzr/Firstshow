package com.example.BookMyShow.Repositories;

import com.example.BookMyShow.Entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Integer> {

    @Query(value = "SELECT * FROM theaters WHERE theater_Id = :theaterId", nativeQuery = true)
    Theater searchForTheater(@Param("theaterId")Integer theaterId);

    @Query(value = "SELECT * FROM theaters WHERE address = :city", nativeQuery = true)
    List<Theater> allTheatersInTheCity(@Param("city")String city);
}







