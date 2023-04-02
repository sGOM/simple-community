package com.project.simplecommunity.exception.custom;

import com.project.simplecommunity.exception.CustomException;
import com.project.simplecommunity.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAccountException extends CustomException {

    private final UserAccountExceptionType userAccountExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return userAccountExceptionType;
    }
}
