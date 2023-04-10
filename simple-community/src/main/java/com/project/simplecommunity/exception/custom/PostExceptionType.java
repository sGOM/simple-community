package com.project.simplecommunity.exception.custom;

import com.project.simplecommunity.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PostExceptionType implements CustomExceptionType {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMsg() {
        return errorMessage;
    }
}
