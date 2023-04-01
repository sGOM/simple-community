package com.project.simplecommunity.exception;

public abstract class CustomException extends RuntimeException {

    public abstract CustomExceptionType getCustomExceptionType();
}
