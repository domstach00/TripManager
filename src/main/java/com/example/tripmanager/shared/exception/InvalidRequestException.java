package com.example.tripmanager.shared.exception;

public class InvalidRequestException extends RuntimeException {
    private static final String MESSAGE = "Invalid request";

    public InvalidRequestException() {
        super(MESSAGE);
    }
}
