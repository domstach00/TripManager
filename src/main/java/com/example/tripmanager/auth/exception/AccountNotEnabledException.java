package com.example.tripmanager.auth.exception;

public class AccountNotEnabledException extends RuntimeException {
    private static final String MESSAGE = "Account is not enabled, please check your email";
    public AccountNotEnabledException() {
        super(MESSAGE);
    }
}
