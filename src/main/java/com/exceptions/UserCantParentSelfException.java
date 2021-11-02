package com.exceptions;

public class UserCantParentSelfException extends RuntimeException {
    public UserCantParentSelfException(String message) {
        super(message);
    }
}
