package com.mobiquity.exception;

/**
 * Implementation of {@link Exception} which denotes a checked exception
 * during main functionality failure.
 *
 * @author Mobiquite
 */
public class APIException extends Exception {

    public APIException(String message, Exception e) {
        super(message, e);
    }

    public APIException(String message) {
        super(message);
    }
}
