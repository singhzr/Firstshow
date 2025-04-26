package com.example.BookMyShow.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponse {
    private String name;
    private String emailId;
    private String username;
    private String password;
    private String JWToken;
}
