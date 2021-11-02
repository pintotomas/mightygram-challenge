package com.exceptions;

public class UserAlreadyLikesPostException extends RuntimeException {
    public UserAlreadyLikesPostException(String message) {
        super(message);
    }
}
