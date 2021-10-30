package com.controllers;

import com.dto.PostResponseDto;
import com.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(PostController.URL_MAPPING_POSTS)
public class PostController {

    public static final String URL_MAPPING_POSTS = "/posts";

    @Autowired private PostService postService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PostResponseDto> getById(@PathVariable Long id) {

        log.info("Requested to get post with id {}", id);
        return ResponseEntity.ok(new PostResponseDto(postService.findById(id)));
    }

}
