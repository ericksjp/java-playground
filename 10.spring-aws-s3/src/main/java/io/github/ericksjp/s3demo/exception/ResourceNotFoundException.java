package io.github.ericksjp.s3demo.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String field, String value) {
        super(String.format("%s with %s '%s' not found", resource, field, value));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
