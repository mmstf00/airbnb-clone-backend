package com.airbnb.aggregatorservice.exception;

public class GrpcServiceException extends RuntimeException {
    public GrpcServiceException(String serviceName, String message, Throwable cause) {
        super("Error calling " + serviceName + ": " + message, cause);
    }
}
