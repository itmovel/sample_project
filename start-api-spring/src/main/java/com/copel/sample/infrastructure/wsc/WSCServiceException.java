package com.copel.sample.infrastructure.wsc;

public class WSCServiceException extends RuntimeException {
    public WSCServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}