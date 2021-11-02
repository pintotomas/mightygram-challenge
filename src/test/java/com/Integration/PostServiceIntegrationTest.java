package com.Integration;

import com.dto.PostCreateRequestDto;
import com.dto.UserParenthoodRequestDto;
import com.dto.UserPostLikeRequestDto;
import com.exceptions.PostNotFoundException;
import com.model.Post;
import com.repositories.PostRepository;
import com.services.PostService;
import com.services.UserService;
import com.services.storage.StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @MockBean
    private StorageService storageService;

    @Test
    void testIntegrationCreateParentHoodWhenChildMakesPostParentDoesntAdoptPost() {
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(2L);
        userService.assignParent(userParenthoodRequestDto);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setOwnerId(2L);
        postCreateRequestDto.setDescription("Description");
        Post post = postService.create(postCreateRequestDto, mockMultipartFile);
        Assertions.assertTrue(postRepository.findByIdAndOwnerId(post.getId(), 2L).isPresent());
        Assertions.assertTrue(postRepository.findByIdAndOwnerId(post.getId(), 1L).isEmpty());
    }

    @Test
    void testIntegrationCreateParentHoodWhenParentMakesPostChildAdoptsPost() {
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(2L);
        userService.assignParent(userParenthoodRequestDto);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setOwnerId(1L);
        postCreateRequestDto.setDescription("Description");
        Post post = postService.create(postCreateRequestDto, mockMultipartFile);
        Assertions.assertTrue(postRepository.findByIdAndOwnerId(post.getId() + 1, 2L).isPresent());
        Assertions.assertTrue(postRepository.findByIdAndOwnerId(post.getId(), 1L).isPresent());
    }

    @Test
    void testIntegrationCreateParentHoodParentMakesPostAndUserLikesParentBothGetOneLike() {
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(2L);
        userService.assignParent(userParenthoodRequestDto);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setOwnerId(1L);
        postCreateRequestDto.setDescription("Description");
        Post parentPost = postService.create(postCreateRequestDto, mockMultipartFile);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(6L);
        userPostLikeRequestDto.setOwnerId(1L);
        parentPost = postService.like(parentPost.getId(), userPostLikeRequestDto);

        Assertions.assertEquals(1L, postService.likeCount(parentPost, userService.findById(1L)));
        Assertions.assertEquals
                (1L, postService.likeCount(postService.findById(parentPost.getId() + 1), userService.findById(2L)));
    }

    @Test
    void testIntegrationCreateParentHoodParentMakesPostAndUserLikesChildOnlyChildGetsOneLike() {
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(2L);
        userService.assignParent(userParenthoodRequestDto);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setOwnerId(1L);
        postCreateRequestDto.setDescription("Description");
        Post parentPost = postService.create(postCreateRequestDto, mockMultipartFile);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(6L);
        userPostLikeRequestDto.setOwnerId(2L);
        Post childPost = postService.like(parentPost.getId() + 1, userPostLikeRequestDto);

        Assertions.assertEquals(0L, postService.likeCount(parentPost, userService.findById(1L)));
        Assertions.assertEquals
                (1L, postService.likeCount(postService.findById(childPost.getId()), userService.findById(2L)));
    }

    @Test
    void testIntegrationCreateParentHoodParentMakesPostAndTryToLikeParentPostWithOwnerIdChildThrowsException() {
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(2L);
        userService.assignParent(userParenthoodRequestDto);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setOwnerId(1L);
        postCreateRequestDto.setDescription("Description");
        Post parentPost = postService.create(postCreateRequestDto, mockMultipartFile);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(6L);
        userPostLikeRequestDto.setOwnerId(2L);
        Assertions.assertThrows(PostNotFoundException.class,
                () -> postService.like(parentPost.getId(), userPostLikeRequestDto));

    }

    @Test
    void testIntegrationCreateParentHoodParentMakesPostAndUserLikesParentAndDislikesBothGetZeroLikes() {
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(2L);
        userService.assignParent(userParenthoodRequestDto);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setOwnerId(1L);
        postCreateRequestDto.setDescription("Description");
        Post parentPost = postService.create(postCreateRequestDto, mockMultipartFile);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(6L);
        userPostLikeRequestDto.setOwnerId(1L);
        parentPost = postService.like(parentPost.getId(), userPostLikeRequestDto);
        postService.dislike(parentPost.getId(), userPostLikeRequestDto);

        Assertions.assertEquals(0L, postService.likeCount(parentPost, userService.findById(1L)));
        Assertions.assertEquals
                (0L, postService.likeCount(postService.findById(parentPost.getId() + 1), userService.findById(2L)));
    }
}
