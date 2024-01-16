package com.airbnb.aggregatorservice.model.response;

import com.airbnb.aggregatorservice.client.ListingDTO;
import com.airbnb.aggregatorservice.client.ReservationDTO;
import com.airbnb.aggregatorservice.client.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AggregatedResponse {
    private final UserDTO user;
    private final ListingDTO listing;
    private final ReservationDTO reservation;
}
