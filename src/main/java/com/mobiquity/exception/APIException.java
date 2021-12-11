package com.mobiquity.exception;

/**
 * Implementation of {@link RuntimeException} which denotes a checked exception
 * during main functionality failure.
 *
 * @author Mobiquite
 */
public class APIException extends RuntimeException {

    public APIException(String message, Exception e) {
        super(message, e);
    }

    public APIException(String message) {
        super(message);
    }
}
