package com.example.BookMyShow.Repositories;

import com.example.BookMyShow.Entities.Theater;
import com.example.BookMyShow.Entities.TheaterSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterSeatsRepository extends JpaRepository<TheaterSeat, Integer> {
}