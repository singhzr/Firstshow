package com.example.BookMyShow.CustomException;

public class InvalidMovieIdException extends Exception{
    public InvalidMovieIdException(String message){
        super(message);
    }

}
