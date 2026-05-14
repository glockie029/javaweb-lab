package com.example.lab02.service;

public class SsrfDemoFetchException extends RuntimeException {

    public SsrfDemoFetchException(String message) {
        super(message);
    }

    public SsrfDemoFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
