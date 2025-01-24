package com.example.tripmanager.account.exception;

public class AccountNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Account was not found";
    public AccountNotFoundException() {
        super(MESSAGE);
    }
}
