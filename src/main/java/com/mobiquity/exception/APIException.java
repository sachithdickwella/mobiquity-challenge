package com.mobiquity.exception;

/**
 * Implementation of {@link RuntimeException} which denotes a checked exception
 * during main functionality failure.
 * <p>
 * Change default implementation of {@link Exception} to {@link RuntimeException}
 * since {@code APIException} represents exceptions during runtime. For example
 * external input data validation cannot consider as compile time exception until
 * the data loaded for evaluation.
 * <p>
 * Besides, using unchecked exception reduce the boilerplate exception handling in
 * {@link java.util.stream.Stream} pipelines.
 *
 * @author Mobiquite
 * @author Sachith Dickwella
 * @version 1.0.0
 * @see RuntimeException
 */
public class APIException extends RuntimeException {

    public APIException(String message, Exception e) {
        super(message, e);
    }

    public APIException(String message) {
        super(message);
    }
}
