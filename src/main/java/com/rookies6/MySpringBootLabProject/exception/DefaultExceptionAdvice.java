package com.rookies6.MySpringBootLabProject.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}