package com.example.BookMyShow.Repositories;

import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.Entities.ShowSeat;
import com.example.BookMyShow.Entities.Ticket;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowSeat s WHERE s.show.showId = :showId AND s.seatNo IN :seatList")
    List<ShowSeat> findSeatsForUpdate(@Param("showId") Integer showId, @Param("seatList") List<String> seatList);

    @Query(value = "SELECT * FROM tickets WHERE ticket_id = :ticketId", nativeQuery = true)
    Ticket findTicket(@Param("ticketId") Integer ticketId);

    @Query(value = "SELECT * FROM tickets WHERE user_user_id = :userId", nativeQuery = true)
    List<Ticket> allTicketsOfTheUser(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tickets WHERE ticket_id = :ticketId", nativeQuery = true)
    void deleteTicketById(@Param("ticketId") Integer ticketId);
}
/*
    1. Without @Param("ticketId"), Spring will not know which method parameter to bind to the :ticketId in the query.
       It will throw an exception because of the missing binding.

    2. The @Modifying annotation in Spring Data JPA is used to indicate that the query is a modifying query,
       which means it performs an update, delete, or insert operation. This annotation is necessary because,
       by default, Spring Data JPA expects all repository query methods to be read-only, i.e.,
       they are expected to execute SELECT statements that do not alter the state of the database.

       Why Use @Modifying
       Indicating a Non-Select Query: @Modifying tells Spring Data JPA that the query is not a SELECT query.
       without this annotation, Spring would treat the query as read-only, expecting a result set to be returned

    3. @Transactional Annotation: This annotation is crucial to ensure that the method is executed within a transaction.
       It guarantees that the database operation either completes successfully or is rolled back in case of failure.

       Why use transactional
       When you execute a delete query without a transactional boundary, each delete operation will be treated as an
       individual transaction. This means that if one delete operation fails, the changes made by previous delete
       operations will not be rolled back, potentially leaving your data in an inconsistent state.

    4. You can change the name of the parameter (:ticketId) in the query to anything you like.
       The key part is that whatever name you choose in the query must match the name you use in the @Param annotation.
       The variable name in your Java method can also be different.

        @Repository
        public interface TicketRepository extends JpaRepository<Ticket, Integer> {

            @Query(value = "SELECT * FROM tickets WHERE ticket_id = :id", nativeQuery = true)
            Ticket findTicket(@Param("id") Integer ticketId); // Renaming the query parameter
        }

       The @Param annotation uses "id" to bind the method argument ticketId to the SQL query parameter.

 */