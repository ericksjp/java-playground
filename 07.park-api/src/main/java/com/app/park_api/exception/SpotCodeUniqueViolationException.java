package com.app.park_api.exception;

public class SpotCodeUniqueViolationException extends RuntimeException {

    public SpotCodeUniqueViolationException(String message) {
        super(message);
    }

}

