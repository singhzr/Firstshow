package com.example.BookMyShow.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service

public class SeatUnlockScheduler {

    @Autowired
    private ShowSeatService showSeatService;

    // Runs every minute to unlock expired seats
    @Scheduled(fixedRate = 1000)
    public void unlockSeats() {
        showSeatService.unlockExpiredSeats();
    }
}

/*
      ---->@Scheduled
      In Spring Boot, @Scheduled is an annotation used to schedule tasks to run at fixed intervals.

      Your ScheduledTasks class looks great! It defines a scheduled task to release ticket holds that have expired.
      The @Scheduled(fixedRate = 60000) annotation ensures that the releaseExpiredTicketHolds method runs every 60 seconds.
*/
