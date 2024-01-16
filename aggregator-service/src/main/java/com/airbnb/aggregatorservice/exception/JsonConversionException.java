package com.airbnb.aggregatorservice.exception;

public class JsonConversionException extends RuntimeException {
    public JsonConversionException(String message, Throwable e) {
        super(message, e);
    }
}
