package com.airbnb.aggregatorservice.service;

import com.airbnb.aggregatorservice.model.request.Request;
import com.airbnb.aggregatorservice.model.response.AggregatedResponse;
import com.airbnb.aggregatorservice.model.response.FavoriteListingsResponse;

public interface AggregatorService {
    AggregatedResponse processRequest(Request request);

    FavoriteListingsResponse getFavoriteListingsForUser(long userId);
}
