package com.goose.mpstest.exception;

import lombok.Getter;

@Getter
public class AirplaneException extends RuntimeException {

    private final ErrorType errorType;

    public AirplaneException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
}
