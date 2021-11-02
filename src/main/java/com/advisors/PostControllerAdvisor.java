package com.advisors;

import com.dto.ErrorResponseDto;
import com.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class PostControllerAdvisor {

    private ErrorResponseDto createErrorResponseDto(ErrorCode errorCode, String message) {

        return new ErrorResponseDto(errorCode, message);
    }

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

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> userNotFoundException(UserNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponseDto(
                ErrorCode.USER_NOT_FOUND, e.getLocalizedMessage()
        ));
    }

}
