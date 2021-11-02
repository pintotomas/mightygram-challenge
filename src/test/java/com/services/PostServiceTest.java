package com.services;

import com.dto.UserPostLikeRequestDto;
import com.exceptions.PostNotFoundException;
import com.exceptions.UserAlreadyLikesPostException;
import com.exceptions.UserDoesNotLikePostException;
import com.model.Post;
import com.model.User;
import com.model.UserPostLike;
import com.repositories.PostRepository;
import com.repositories.UserPostLikeRepository;
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
        Post post = new Post("description", "url");
        post.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Post foundPost = postService.findById(1L);
        Assertions.assertEquals(1L, foundPost.getId());
        Assertions.assertEquals("description", foundPost.getDescription());
        Assertions.assertEquals("url", foundPost.getPhotoUrl());
    }

    @Test
    void testFindByIdThrowsException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(PostNotFoundException.class, () -> postService.findById(1L));
    }

    @Test
    void testLikeAlreadyLikedPostThrowsException() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(new Post()));
        when(userService.findById(1L)).thenReturn(new User());
        when(userPostLikeRepository.existsByUserPostLikeIdUserIdAndUserPostLikeIdPostId(1L, 1L)).thenReturn(true);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setUserId(1L);
        Assertions.assertThrows(UserAlreadyLikesPostException.class, () -> postService.like(1L, userPostLikeRequestDto));
    }

    @Test
    void testLikePostSuccess() {
        User user = new User("userTest");
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);
        Post post = new Post("description", "url");
        post.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setUserId(1L);
        postService.like(1L, userPostLikeRequestDto);
        verify(userPostLikeRepository, times(1)).save(userPostLikeArgumentCaptor.capture());
        verify(postRepository, times(1)).save(postArgumentCaptor.capture());
        Assertions.assertEquals
                (1L, userPostLikeArgumentCaptor.getValue().getUserPostLikeId().getPostId());
        Assertions.assertEquals(1L, userPostLikeArgumentCaptor.getValue().getUserPostLikeId().getUserId());
    }

    @Test
    void testDoesntLikePostException() {
        when(userPostLikeRepository.existsByUserPostLikeIdUserIdAndUserPostLikeIdPostId(1L, 1L)).thenReturn(false);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setUserId(1L);
        Assertions.assertThrows(UserDoesNotLikePostException.class, () -> postService.dislike(1L, userPostLikeRequestDto));
    }

    @Test
    void testDislikePostSuccess() {

        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setUserId(1L);
        when(userPostLikeRepository.existsByUserPostLikeIdUserIdAndUserPostLikeIdPostId(1L, 1L)).thenReturn(true);
        postService.dislike(1L, userPostLikeRequestDto);
        verify(userPostLikeRepository, times(1)).
                deleteByUserPostLikeIdUserIdAndUserPostLikeIdPostId(longArgumentCaptor.capture(), longArgumentCaptor.capture());
    }
}
