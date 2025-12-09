package com.copel.sample.infrastructure.wsc;

public class WSCBusinessException extends RuntimeException {
    public WSCBusinessException(String message) {
        super(message);
    }
}