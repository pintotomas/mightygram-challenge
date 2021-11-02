package com.advisors;

import com.dto.ErrorResponseDto;
import com.exceptions.ErrorCode;
import com.exceptions.StorageException;
import com.exceptions.StorageFileNotFoundException;
import com.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GeneralControllerAdvisor {

    protected ErrorResponseDto createErrorResponseDto(ErrorCode errorCode, String message) {

        return new ErrorResponseDto(errorCode, message);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> userNotFoundException(UserNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponseDto(
                ErrorCode.USER_NOT_FOUND, e.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = StorageException.class)
    public ResponseEntity<ErrorResponseDto> storageException(StorageException e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponseDto(
                ErrorCode.STORAGE_ERROR, e.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = StorageFileNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> storageFileNotFoundException(StorageFileNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponseDto(
                ErrorCode.FILE_NOT_FOUND, e.getLocalizedMessage()
        ));
    }
}
