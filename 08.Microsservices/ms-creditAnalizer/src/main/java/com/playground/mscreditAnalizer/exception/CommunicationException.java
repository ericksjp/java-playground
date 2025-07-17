package com.playground.mscreditAnalizer.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommunicationException extends RuntimeException {
    private Integer statusCode;

    public CommunicationException(Integer statusCode, String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
    }
}
