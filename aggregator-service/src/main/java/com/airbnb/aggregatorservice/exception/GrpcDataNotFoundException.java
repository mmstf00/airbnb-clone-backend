package com.airbnb.aggregatorservice.exception;

public class GrpcDataNotFoundException extends RuntimeException {

    public GrpcDataNotFoundException(String serviceName, String message, Throwable cause) {
        super("Data is not available in " + serviceName + ": " + message, cause);
    }
}
