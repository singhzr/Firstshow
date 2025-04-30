package com.example.BookMyShow.Repositories;

import com.example.BookMyShow.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmailId(String emailId);
    User findByUserId(Integer userId);
    @Query(value = "SELECT * FROM users WHERE email_id = :emailId", nativeQuery = true)
    User customMethod(String emailId);

    User findByUserName(String username);
}

//1. If nativeQuery = false. This means Spring is expecting Jpa Query Language
//   If nativeQuery = true. This means Spring is expecting SQL Query Language

//2. = : If we have this written in query this means it will take parameter from below method