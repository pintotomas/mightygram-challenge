package com.services;

import com.dto.PostCreateRequestDto;
import com.dto.UserPostLikeRequestDto;
import com.exceptions.PostNotFoundException;
import com.exceptions.UserAlreadyLikesPostException;
import com.exceptions.UserDoesNotLikePostException;
import com.model.Post;
import com.model.User;
import com.model.UserPostLike;
import com.repositories.PostRepository;
import com.repositories.UserPostLikeRepository;
import com.services.storage.StorageService;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserPostLikeRepository userPostLikeRepository;

    @Mock
    private UserService userService;

    @Mock
    private StorageService storageService;

    @Rule
    private MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    @Captor
    private ArgumentCaptor<UserPostLike> userPostLikeArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void testFindByIdSuccess() {
        Post post = new Post("description", "url", new User());
        post.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Post foundPost = postService.findById(1L);
        Assertions.assertEquals(1L, foundPost.getId());
        Assertions.assertEquals("description", foundPost.getDescription());
        Assertions.assertEquals("url", foundPost.getFilename());
    }

    @Test
    void testFindByIdThrowsException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(PostNotFoundException.class, () -> postService.findById(1L));
    }

    @Test
    void testLikeAlreadyLikedPostThrowsException() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(new Post()));
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);
        when(userPostLikeRepository.
                existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId
                        (1L, 1L, 1L)).thenReturn(true);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        Assertions.assertThrows(UserAlreadyLikesPostException.class,
                () -> postService.like(1L, userPostLikeRequestDto));
    }

    @Test
    void testLikePostSuccess() {
        User user = new User("userTest");
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);
        Post post = new Post("description", "url", new User());
        post.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        postService.like(1L, userPostLikeRequestDto);
        verify(userPostLikeRepository, times(1)).save(userPostLikeArgumentCaptor.capture());
        verify(postRepository, times(1)).save(postArgumentCaptor.capture());
        Assertions.assertEquals
                (1L, userPostLikeArgumentCaptor.getValue().getUserPostLikeId().getPostId());
        Assertions.assertEquals(1L, userPostLikeArgumentCaptor.getValue().getUserPostLikeId().getLikerId());
    }

    @Test
    void testDoesntLikePostException() {
        when(userPostLikeRepository.
                existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId
                        (1L, 1L, 1L)).thenReturn(false);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        Assertions.assertThrows(UserDoesNotLikePostException.class,
                () -> postService.dislike(1L, userPostLikeRequestDto));
    }

    @Test
    void testDislikePostSuccess() {

        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        when(userPostLikeRepository.
                existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId
                        (1L, 1L, 1L)).thenReturn(true);
        postService.dislike(1L, userPostLikeRequestDto);
        verify(userPostLikeRepository, times(1)).
                deleteByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId
                        (longArgumentCaptor.capture(), longArgumentCaptor.capture(), longArgumentCaptor.capture());
    }

    @Test
    void testCreatePost() {
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setDescription("description");
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());
        postService.create(postCreateRequestDto, mockMultipartFile);
        verify(postRepository, times(1)).save(postArgumentCaptor.capture());
        Assertions.assertTrue(postArgumentCaptor.getValue().getFilename().contains("file"));
        Assertions.assertTrue(postArgumentCaptor.getValue().getDescription().equals("description"));
    }

    @Test
    void testCreatePostParentWithOneChild() {
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setDescription("description");
        postCreateRequestDto.setOwnerId(6L);
        User child = new User();
        child.setId(7L);
        User user = new User();
        user.setId(6L);
        user.setUsername("username");
        when(userService.findById(6L)).thenReturn(user);
        when(userService.findAllChildren(user)).thenReturn(Arrays.asList(child));
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());
        postService.create(postCreateRequestDto, mockMultipartFile);
        verify(postRepository, times(2)).save(postArgumentCaptor.capture());
        Assertions.assertTrue(postArgumentCaptor.getAllValues().get(0).getFilename().contains("file"));
        Assertions.assertTrue(postArgumentCaptor.getAllValues().get(0).getDescription().equals("description"));
        Assertions.assertTrue(postArgumentCaptor.getAllValues().get(0).getOwner().equals(user));
        Assertions.assertTrue(postArgumentCaptor.getAllValues().get(1).getFilename().contains("file"));
        Assertions.assertTrue(postArgumentCaptor.getAllValues().get(1).getDescription().equals("description"));
        Assertions.assertTrue(postArgumentCaptor.getAllValues().get(1).getOwner().equals(child));
    }


}
