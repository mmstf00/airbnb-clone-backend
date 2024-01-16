package com.airbnb.aggregatorservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String NOT_FOUND_MESSAGE = "Requested data may not exist";

    @ExceptionHandler({CompletionException.class, GrpcDataNotFoundException.class})
    public ResponseEntity<Object> handleGrpcDataNotAvailable() {
        return errorWithTimestamp(NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GrpcServiceException.class)
    public ResponseEntity<Object> handleGrpcServiceNotAvailableException() {
        return errorWithTimestamp("gRPC service is not available", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> errorWithTimestamp(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
