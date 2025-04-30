package com.example.BookMyShow.Repositories;

import com.example.BookMyShow.Entities.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {
    @Query(value = "SELECT * FROM shows", nativeQuery = true)
    List<Show> findAllShows();
}
