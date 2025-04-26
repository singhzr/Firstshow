package com.example.BookMyShow.RequestDTOs;

import lombok.Data;

@Data //for getting Getters and Setters
public class AddUserRequest {

    private String name;
    private String emailId;
    private String userName;
    private String password;
    private String roles;
}
