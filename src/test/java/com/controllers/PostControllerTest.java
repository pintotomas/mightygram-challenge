package com.controllers;

import com.advisors.PostControllerAdvisor;
import com.exceptions.PostNotFoundException;
import com.model.Post;
import com.services.PostService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @InjectMocks
    PostController postController;

    @Mock
    PostService postService;

    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;

    private MockMvc mockMvc;

    private Post post;

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new PostControllerAdvisor())
                .build();
        post = new Post("description", "url");
        LocalDateTime localDateTime = LocalDateTime.of(2021, 10, 30, 16, 39);
        post.setCreated(localDateTime);
    }

    @Test
    void testGetByKnownIdShouldReturnOkStatus() throws Exception {
        when(postService.findById(1L)).thenReturn(post);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(postController.URL_MAPPING_POSTS + "/1");
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetByUnknownIdShouldReturnNotFoundStatus() throws Exception {
        when(postService.findById(2L)).thenThrow(PostNotFoundException.class);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(postController.URL_MAPPING_POSTS + "/2");
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetByKnownIdShouldReturnJson() throws Exception {
        when(postService.findById(1L)).thenReturn(post);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(postController.URL_MAPPING_POSTS + "/1");
        mockMvc.perform(requestBuilder).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetByIdShouldCallPostServiceFindById() throws Exception {
        when(postService.findById(1L)).thenReturn(post);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(postController.URL_MAPPING_POSTS + "/1");
        mockMvc.perform(requestBuilder);
        verify(postService, times(1)).findById(longArgumentCaptor.capture());
        Assertions.assertEquals(1L, longArgumentCaptor.getValue());
    }

    @Test
    void testGetByIdContentReturnsPost() throws Exception {
        when(postService.findById(1L)).thenReturn(post);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(postController.URL_MAPPING_POSTS + "/1");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals("description", json.get("description"));
        Assertions.assertEquals("url", json.get("photoUrl"));
        Assertions.assertEquals("2021-10-30T16:39:00", json.get("created"));
        Assertions.assertEquals(4, json.length());
    }


}
