package com.playground.mscreditAnalizer.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.playground.mscreditAnalizer.exception.CommunicationException;
import com.playground.mscreditAnalizer.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<SimpleErrorMessage> handleResourceNotFound(ResourceNotFoundException ex) {
        SimpleErrorMessage message = new SimpleErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(CommunicationException.class)
    public ResponseEntity<SimpleErrorMessage> handleCommunicationException(CommunicationException ex) {
        SimpleErrorMessage message = new SimpleErrorMessage(ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(message);
    }
    
}
