package com.kamilcieslik.pwr.bus.commons;

import lombok.Getter;

@Getter
public class InternalException extends RuntimeException {
    private String errorCode;

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
