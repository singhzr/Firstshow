package com.example.BookMyShow.CustomException;

public class InvalidTheaterException extends Exception{
    public InvalidTheaterException(String message) {
        super(message);
    }
}
