
package com.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {

    POST_NOT_FOUND(101),
    POST_NOT_LIKED(102),
    POST_ALREADY_LIKED(103),
    USER_NOT_FOUND(104),
    CONSTRAINT_VIOLATION(105),
    STORAGE_ERROR(106),
    FILE_NOT_FOUND(107);

    private Integer errorNumber;

    ErrorCode(Integer errorNumber) {
        this.errorNumber = errorNumber;
    }
}