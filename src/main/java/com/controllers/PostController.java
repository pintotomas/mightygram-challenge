package com.controllers;

import com.dto.PostResponseDto;
import com.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Optional;

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

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<Page<PostResponseDto>> getAllPostsPaginated(
            @RequestParam("page") @Min(1) Integer page,
            @RequestParam("size") @Min(1) Integer size
    ) {

        log.info("Requested to get posts page {} size {}", page, size);
        return ResponseEntity.ok(postService.findAllPaginated(page, size).map(post -> new PostResponseDto(post)));
    }
}
