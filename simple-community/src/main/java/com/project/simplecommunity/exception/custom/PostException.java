package com.project.simplecommunity.exception.custom;

import com.project.simplecommunity.exception.CustomException;
import com.project.simplecommunity.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostException extends CustomException {
    private final PostExceptionType postExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return postExceptionType;
    }
}
