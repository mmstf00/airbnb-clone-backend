package com.airbnb.aggregatorservice.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Request {
    private long userId;
    private long listingId;
    private long reservationId;
}
