package com.web.mongo.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Object id) {
        super("Object not found. Id " + id);
    }
}
