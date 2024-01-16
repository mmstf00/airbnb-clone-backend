package com.airbnb.aggregatorservice.util;

import com.airbnb.aggregatorservice.exception.JsonConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

public class ProtobufUtil {

    private ProtobufUtil() {
        throw new UnsupportedOperationException("Cannot create instance of utility class");
    }

    /**
     * Converts a Protocol Buffer message to a Java Object.
     * <br>
     * Proto -> Json -> Java
     * <br>
     * Conversion from Proto to JSON then Java DTO mapping is 2x slower.
     * Use {@link GRPCMapper} for direct mapping Proto -> DTO mapping.
     *
     * @param messageOrBuilder The Protocol Buffer message to convert.
     * @return An Object representation of the input message.
     * @throws JsonConversionException If an error occurs during the conversion.
     */
    public static <T> T toObject(MessageOrBuilder messageOrBuilder, Class<T> tClass) {
        try {
            return new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .readValue(toJson(messageOrBuilder), tClass);
        } catch (JsonMappingException e) {
            throw new JsonConversionException("Error occurred while mapping JSON", e);
        } catch (JsonProcessingException e) {
            throw new JsonConversionException("Error occurred while processing JSON", e);
        }
    }

    /**
     * Converts a Protocol Buffer message to a JSON string.
     *
     * @param messageOrBuilder The Protocol Buffer message to convert.
     * @return A JSON representation of the input message.
     * @throws JsonConversionException If an error occurs during the conversion.
     */
    public static String toJson(MessageOrBuilder messageOrBuilder) {
        try {
            return JsonFormat.printer().print(messageOrBuilder);
        } catch (InvalidProtocolBufferException e) {
            throw new JsonConversionException("Error occurred while converting protobuf message to JSON", e);
        }
    }

}
