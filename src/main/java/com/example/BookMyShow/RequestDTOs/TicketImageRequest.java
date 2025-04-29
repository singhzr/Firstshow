package com.example.BookMyShow.RequestDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketImageRequest {
    private String email;
    private String imageBase64;
}
