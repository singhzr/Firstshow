package com.example.BookMyShow.CustomException;

public class InvalidShowException extends Exception{
    public InvalidShowException(String message) {
        super(message);
    }
}
