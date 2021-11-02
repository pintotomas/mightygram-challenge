package com.advisors;

import com.dto.ErrorResponseDto;
import com.exceptions.ErrorCode;
import com.exceptions.UserAlreadyHasAParentException;
import com.exceptions.UserCantParentSelfException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvisor extends GeneralControllerAdvisor {

    @ExceptionHandler(value = UserAlreadyHasAParentException.class)
    public ResponseEntity<ErrorResponseDto> userAlreadyHasAParentException(UserAlreadyHasAParentException e) {

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(createErrorResponseDto(
                ErrorCode.USER_ALREADY_HAS_PARENT, e.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = UserCantParentSelfException.class)
    public ResponseEntity<ErrorResponseDto> userCantParentSelfException(UserCantParentSelfException e) {

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(createErrorResponseDto(
                ErrorCode.USER_CANT_PARENT_SELF, e.getLocalizedMessage()
        ));
    }
}
