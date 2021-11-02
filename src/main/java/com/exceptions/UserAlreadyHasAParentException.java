package com.exceptions;

public class UserAlreadyHasAParentException extends RuntimeException {
    public UserAlreadyHasAParentException(String message) {
        super(message);
    }
}
