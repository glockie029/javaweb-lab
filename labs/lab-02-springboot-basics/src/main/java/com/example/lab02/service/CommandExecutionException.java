package com.example.lab02.service;

public class CommandExecutionException extends RuntimeException {

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
