package com.example.BookMyShow.Repositories;

import com.example.BookMyShow.Entities.ShowSeat;
import com.example.BookMyShow.Enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Integer> {
    List<ShowSeat> findShowSeatByShowShowId(Integer showId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowSeat s WHERE s.show.showId = :showId AND s.seatNo IN :seatList")
    List<ShowSeat> findSeatsForUpdate(@Param("showId") Integer showId, @Param("seatList") List<String> seatList);
    List<ShowSeat> findByStatusAndLockTimeBefore(SeatStatus status, LocalDateTime cutoffTime);
}
/*

---->@Lock(LockModeType.PESSIMISTIC_WRITE) is used in JPA (Java Persistence API) to apply pessimistic
      locking when retrieving an entity from the database. This kind of lock is especially useful in scenarios
      where you want to prevent other transactions from reading or modifying the same row(s) until your current
      transaction completes.

        Meaning of LockModeType.PESSIMISTIC_WRITE:
        It acquires a database-level write lock on the selected row(s).

        Other transactions trying to read or write the same data will be blocked until your transaction releases the
        lock (usually after a commit or rollback).

        It ensures data consistency in high-concurrency environments.


---->Why are we using  @Transactional and FOR UPDATE
    A row locked with FOR UPDATE is only locked until the transaction completes — either by:

    Committing (successful end of the @Transactional method), or

    Rolling back (an exception happens, and the transaction fails).

    Here's What Happens Behind the Scenes:
    You start a method with @Transactional.

    Inside that method, you run a query with FOR UPDATE → it locks the row.

    While the method is running:

    No other transaction can lock, update, or even read that row (depending on DB isolation).

    When the method ends:

    If successful → Spring calls commit(), and the lock is released.

    If there’s an error → Spring rolls back, and the lock is also released.

    Why This Is Important
    If you didn’t use @Transactional, your FOR UPDATE would still lock, but it would auto-commit right after the query,
    and other threads could jump in — making the lock useless.

    So, @Transactional is what holds the lock in place while you do your seat status checks and updates.


---->What is FOR UPDATE?
    FOR UPDATE is a SQL clause used in a SELECT query to apply a pessimistic lock on the rows it returns.

    It locks the selected rows so that:

    Other transactions cannot modify or lock them

    The lock is held until the transaction completes


---->What is @Transactional?

    @Transactional tells Spring: “Wrap this method in a database transaction.”

    That means:
    1.All DB operations inside this method are treated as a single unit of work
    2.If everything succeeds → commit
    3.If something fails → rollback

    Why Use It?

    1.Data Integrity
    Prevents partial updates.
    If any part fails, the DB rolls back to its original state.

    2.Locks Work Properly
    Pessimistic locks like FOR UPDATE only hold until the end of a transaction.
    Without @Transactional, Spring opens & closes the DB connection too fast — locks won’t hold.

    3.Simplified Error Handling
    No need to manually manage commit/rollback in most cases.


---->What is a Pessimistic Lock?
    A pessimistic lock assumes that conflicts will happen, so it locks the data early to prevent others from accessing it.
    “I don’t trust others — I’ll lock the row right now so no one else can touch it while I’m working.”

    How It Works:
    1.When a transaction reads a row with a pessimistic lock, it locks that row.

    2.No other transaction can:
    a)Read it (in some isolation levels)
    b)Update it
    c)Delete it

    The lock is held until the transaction finishes (via commit or rollback).

    Why Use It?

    To avoid race conditions, especially when:

    1.Many users might try to modify the same data (like booking the same seat).
    2.Data consistency is critical.
    3.You can't afford retries or conflicts (like in financial or booking systems).

 */