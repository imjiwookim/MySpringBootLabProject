package com.rookies6.MySpringBootLabProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class DefaultExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorObject> handleBusinessException(BusinessException e) {
        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(e.getHttpStatus().value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getHttpStatus()).body(errorObject);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(errorObject);
    }
}