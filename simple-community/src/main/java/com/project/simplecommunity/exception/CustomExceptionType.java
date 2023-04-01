package com.project.simplecommunity.exception;

import org.springframework.http.HttpStatus;

public interface CustomExceptionType {

    HttpStatus getHttpStatus();
    String getErrorMsg();
}
