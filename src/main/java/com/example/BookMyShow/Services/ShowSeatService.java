package com.example.BookMyShow.Services;
import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.Entities.ShowSeat;
import com.example.BookMyShow.Enums.SeatStatus;
import com.example.BookMyShow.Repositories.ShowRepository;
import com.example.BookMyShow.Repositories.ShowSeatRepository;
import com.example.BookMyShow.RequestDTOs.LockShowSeatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowSeatService {

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private ShowRepository showRepository;

    @Transactional
    public String lockSeat(LockShowSeatRequest lockShowSeatRequest, String sessionId) {
        try {
            // Try to fetch the seat with a pessimistic write lock to prevent concurrency
            List<ShowSeat> showSeatList = showSeatRepository.findSeatsForUpdate(lockShowSeatRequest.getShowId(),
                    lockShowSeatRequest.getSeatList());


            // Check if the seat is available for locking
            for (ShowSeat showSeat : showSeatList) {
                if (showSeat.getStatus() != SeatStatus.AVAILABLE) {
                    return "Seats already locked or booked.";
                }
            }

            for (ShowSeat showSeat : showSeatList) {
                showSeat.setStatus(SeatStatus.LOCKED);
                showSeat.setLockedBy(sessionId);
                showSeat.setLockTime(LocalDateTime.now());
                showSeatRepository.save(showSeat);
            }

            // Save the changes to DB
            return "Seats locked successfully.";

        } catch (Exception e) {
            // Handle any concurrency or timeout exceptions
            return "Failed to lock seat due to concurrent access. Please try again.";
        }
    }

    @Transactional
    public void unlockExpiredSeats() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(5);
        List<ShowSeat> expiredLockedSeats = showSeatRepository.findByStatusAndLockTimeBefore(SeatStatus.LOCKED, cutoff);

        for (ShowSeat showSeat : expiredLockedSeats) {
            showSeat.setStatus(SeatStatus.AVAILABLE);
            showSeat.setLockedBy(null);
            showSeat.setLockTime(null);
        }

        showSeatRepository.saveAll(expiredLockedSeats); // Save updated seats back to DB
    }
}
