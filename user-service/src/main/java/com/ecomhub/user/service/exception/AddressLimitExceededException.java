package com.ecomhub.user.service.exception;

public class AddressLimitExceededException extends RuntimeException {
    public AddressLimitExceededException(String message) {
        super(message);
    }
}