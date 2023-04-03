package com.project.simplecommunity.exception.custom;

import com.project.simplecommunity.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserAccountExceptionType implements CustomExceptionType {

    USER_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.");

    private final HttpStatus httpStatus;
    private final String errorMsg;

    @Override
    public HttpStatus getHttpStatus() { return httpStatus; }

    @Override
    public String getErrorMsg() { return errorMsg; }
}
