package com.airbnb.aggregatorservice.handler;

import com.airbnb.aggregatorservice.exception.GrpcDataNotFoundException;
import com.airbnb.aggregatorservice.exception.GrpcServiceException;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionException;

public class GrpcCallHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcCallHandler.class);

    /**
     * Handles the completion of a CompletableFuture if any problem occur when making async request.
     *
     * @param result      The result of the asynchronous operation.
     * @param throwable   The throwable representing any exception that occurred during the operation.
     * @param serviceName The name of the service for which the operation was performed.
     * @param <T>         The type of the result.
     * @return The result of the asynchronous operation if successful; otherwise,
     * the default or fallback value as appropriate.
     * @throws CompletionException If an exception occurred during the asynchronous operation.
     */
    public static <T> T handleAsync(T result, Throwable throwable, String serviceName) {
        if (throwable != null) {
            LOGGER.error("Error fetching {} : {}", serviceName, throwable.getMessage());
            throw new CompletionException("Failed to fetch " + serviceName, throwable);
        }
        return result;
    }

    /**
     * Handles gRPC calls by invoking the provided {@link GrpcCall} function and managing exceptions.
     *
     * @param serviceName The name of the gRPC service for which the call is made.
     * @param grpcCall    The functional interface representing the gRPC call to be executed.
     * @param <T>         The type of the result from the gRPC call.
     * @return The result of the gRPC call if successful; otherwise, null or an exception is thrown.
     * @throws GrpcServiceException If a gRPC-specific error occurs during the call.
     */
    public static <T> T handleSync(String serviceName, GrpcCall<T> grpcCall) {
        try {
            return grpcCall.call();
        } catch (StatusRuntimeException e) {
            handleGrpcException(serviceName, e);
        } catch (Exception e) {
            throw new GrpcServiceException(serviceName, "Unexpected error", e);
        }
        return null;
    }

    /**
     * Handles gRPC-specific exceptions and throws custom exceptions based on the error code.
     *
     * @param serviceName The name of the gRPC service for which the exception occurred.
     * @param e           The {@link StatusRuntimeException} representing the gRPC-specific exception.
     * @throws GrpcServiceException      If a general gRPC error occurs.
     * @throws GrpcDataNotFoundException If the requested data is not available.
     */
    private static void handleGrpcException(String serviceName, StatusRuntimeException e) {
        if (e.getStatus().getCode() == io.grpc.Status.Code.UNAVAILABLE) {
            // Handle service unavailable
            LOGGER.error("Service {} unavailable: {}", serviceName, e.getMessage());
            throw new GrpcServiceException(serviceName, e.getMessage(), e);
        } else if (e.getStatus().getCode() == io.grpc.Status.Code.UNKNOWN) {
            // Handle data is not available
            LOGGER.error("Requested data is not available in {} service", serviceName);
            throw new GrpcDataNotFoundException(serviceName, e.getMessage(), e);
        } else {
            // Other gRPC-specific errors
            LOGGER.error("gRPC error: {}", e.getMessage());
        }
    }

    @FunctionalInterface
    public interface GrpcCall<T> {
        T call() throws StatusRuntimeException;
    }
}
