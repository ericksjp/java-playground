package com.app.park_api.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResourceNotFoundException extends RuntimeException {

    public static final String templateKey = "exception.resourceNotFound";

    private String resource;
    private String field;
    private String value;

    public ResourceNotFoundException(String resource, String field, String value) {
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}
