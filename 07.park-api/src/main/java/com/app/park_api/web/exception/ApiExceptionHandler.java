package com.app.park_api.web.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.park_api.exception.InvalidPasswordException;
import com.app.park_api.exception.ResourceNotFoundException;
import com.app.park_api.exception.ResourceUniqueViolationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleJsonError(HttpMediaTypeNotSupportedException er, HttpServletRequest request) {
        log.error("Api error - ", er);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, er.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleJsonError(HttpMessageNotReadableException er, HttpServletRequest request) {
        log.error("Api error - ", er);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "invalid request payload"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> acessDeniedException(AccessDeniedException er, HttpServletRequest request) {
        log.error("Api error - ", er);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, er.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorMessage> passwordInvalidException(InvalidPasswordException er, HttpServletRequest request) {
        log.error("Api error - ", er);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, er.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException er, HttpServletRequest request) {
        log.error("Api error - ", er);
        String message = messageSource.getMessage(
                ResourceNotFoundException.templateKey,
                new Object[]{er.getResource(), er.getField(), er.getValue()},
                request.getLocale()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
    }

    @ExceptionHandler(ResourceUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> usernameUniqueViolationException(ResourceUniqueViolationException er, HttpServletRequest request) {
        log.error("Api error - ", er);
        String message = messageSource.getMessage(
                ResourceUniqueViolationException.templateKey,
                new Object[]{er.getResource(), er.getField(), er.getValue()},
                request.getLocale()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(
        MethodArgumentNotValidException er,
        HttpServletRequest request,
        BindingResult result
    ) {
        log.error("Api error - ", er);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(
                        request, 
                        HttpStatus.UNPROCESSABLE_ENTITY, 
                        messageSource.getMessage("message.invalid.field", null, request.getLocale()),
                        result, 
                        messageSource
                    )
                );
    }

}
