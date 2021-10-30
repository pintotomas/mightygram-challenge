package com.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(PostController.URL_MAPPING_POSTS)
public class PostController {

    public static final String URL_MAPPING_POSTS = "/posts";

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable Long id) {

        log.info("Requested to get post with id {}", id);
        return ResponseEntity.ok("Hello");
    }

}
