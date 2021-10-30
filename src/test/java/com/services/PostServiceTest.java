package com.services;

import com.exceptions.PostNotFoundException;
import com.model.Post;
import com.repositories.PostRepository;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

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
}
