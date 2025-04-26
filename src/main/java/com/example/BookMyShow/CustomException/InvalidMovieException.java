package com.example.BookMyShow.CustomException;

public class InvalidMovieException extends Exception{
    public InvalidMovieException(String message) {
        super(message);
    }
}
