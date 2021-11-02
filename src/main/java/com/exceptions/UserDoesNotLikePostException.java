package com.exceptions;

public class UserDoesNotLikePostException extends RuntimeException {
    public UserDoesNotLikePostException(String message) {
        super(message);
    }
}
