package com.dto;

import com.exceptions.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponseDto {

    private String errorCode;
    private Integer errorNumber;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode, String message) {
        this.errorCode = errorCode.name();
        this.errorNumber = errorCode.getErrorNumber();
        this.message = message;
    }
}