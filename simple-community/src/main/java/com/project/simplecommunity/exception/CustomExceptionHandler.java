package com.project.simplecommunity.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> customExceptionHandler(CustomException e) {
        return ResponseEntity.status(e.getCustomExceptionType().getHttpStatus())
                .body(e.getCustomExceptionType().getErrorMsg());
    }
}
