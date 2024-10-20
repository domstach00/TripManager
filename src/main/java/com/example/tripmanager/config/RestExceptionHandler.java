package com.example.tripmanager.config;

import com.example.tripmanager.exception.AccountAlreadyExistsException;
import com.example.tripmanager.exception.AccountNotFoundException;
import com.example.tripmanager.exception.InvalidRequestException;
import com.example.tripmanager.exception.WrongCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.FailedLoginException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = { AccountNotFoundException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(value = { WrongCredentialsException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(WrongCredentialsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(value = { AccountAlreadyExistsException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(AccountAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = { InvalidRequestException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(InvalidRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = { FailedLoginException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(FailedLoginException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}
