package com.controllers;

import com.dto.PostCreateRequestDto;
import com.dto.PostResponseDto;
import com.dto.UserPostLikeRequestDto;
import com.model.Post;
import com.services.PostService;
import com.validations.annotations.ValidFile;
import com.validations.enums.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@Slf4j
@RequestMapping(PostController.URL_MAPPING_POSTS)
@Validated
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

    @PostMapping("/{id}/like")
    @ResponseBody
    public ResponseEntity<PostResponseDto> likePost(
            @PathVariable Long id,
            @RequestBody @Validated UserPostLikeRequestDto userPostLikeRequestDto
    ) {
        log.info("Requested to like post {} from user {}",
                id, userPostLikeRequestDto.getUserId());
        return ResponseEntity.ok(new PostResponseDto(postService.like(id, userPostLikeRequestDto)));
    }

    @PostMapping("/{id}/dislike")
    @ResponseBody
    public ResponseEntity<PostResponseDto> dislikePost(
            @PathVariable Long id,
            @RequestBody @Validated UserPostLikeRequestDto userPostLikeRequestDto
    ) {
        log.info("Requested to dislike post {} from user {}",
                id, userPostLikeRequestDto.getUserId());
        return ResponseEntity.ok(new PostResponseDto(postService.dislike(id, userPostLikeRequestDto)));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<PostResponseDto> createPost(
            @RequestParam("photo")
            @Validated
            @ValidFile(type = {FileType.IMAGE_JPEG, FileType.IMAGE_PNG})
            MultipartFile photo,
            @ModelAttribute @Validated PostCreateRequestDto postCreateRequestDto) {
        Post post = new Post();
        post.setCreated(LocalDateTime.now());
        post.setUserPostLikes(Arrays.asList());
        return ResponseEntity.ok(new PostResponseDto(post));
    }
}
