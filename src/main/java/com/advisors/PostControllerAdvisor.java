package com.advisors;

import com.dto.ErrorResponseDto;
import com.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class PostControllerAdvisor extends GeneralControllerAdvisor {

    @ExceptionHandler(value = PostNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> postNotFoundException(PostNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponseDto(
                ErrorCode.POST_NOT_FOUND, e.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = UserAlreadyLikesPostException.class)
    public ResponseEntity<ErrorResponseDto> userAlreadyLikesPostException(UserAlreadyLikesPostException e) {

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(createErrorResponseDto(
                ErrorCode.POST_ALREADY_LIKED, e.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = UserDoesNotLikePostException.class)
    public ResponseEntity<ErrorResponseDto> userDoesNotLikePostException(UserDoesNotLikePostException e) {

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(createErrorResponseDto(
                ErrorCode.POST_NOT_LIKED, e.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> constraintViolationException(ConstraintViolationException e) {

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(createErrorResponseDto(
                ErrorCode.CONSTRAINT_VIOLATION, e.getLocalizedMessage()
        ));
    }

}
