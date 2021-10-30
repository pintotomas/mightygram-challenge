package com.advisors;

import com.exceptions.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class PostControllerAdvisor {

    @ExceptionHandler(value = PostNotFoundException.class)
    public ResponseEntity<?> postNotFoundException() {

        return ResponseEntity.notFound().build();
    }

}
