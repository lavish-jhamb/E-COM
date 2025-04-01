package com.ecomhub.user.service.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
