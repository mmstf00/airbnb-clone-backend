package com.airbnb.aggregatorservice.model.response;

import com.airbnb.aggregatorservice.client.ListingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FavoriteListingsResponse {
    private List<ListingDTO> favorites;
}
