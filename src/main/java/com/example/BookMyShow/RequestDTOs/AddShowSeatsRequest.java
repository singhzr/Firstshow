package com.example.BookMyShow.RequestDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddShowSeatsRequest {

    private int priceForClassicSeats;
    private int priceForPremiumSeats;
    private int showId;
}
