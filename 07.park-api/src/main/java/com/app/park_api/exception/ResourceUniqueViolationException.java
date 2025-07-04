package com.app.park_api.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResourceUniqueViolationException extends RuntimeException {

    public static final String templateKey = "exception.resourceUniqueViolation";

    private String resource;
    private String field;
    private String value;

    public ResourceUniqueViolationException(String resource, String field, String value) {
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}

