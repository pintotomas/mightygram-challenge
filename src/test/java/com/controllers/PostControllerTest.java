package com.controllers;

import com.advisors.PostControllerAdvisor;
import com.dto.PostCreateRequestDto;
import com.dto.UserPostLikeRequestDto;
import com.exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.model.Post;
import com.model.User;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    private MockMvc mockMvc;

    private Post post;

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new PostControllerAdvisor())
                .build();
        post = new Post("description", "url", new User(), null);
        LocalDateTime localDateTime = LocalDateTime.of(2021, 10, 30, 16, 39);
        post.setCreated(localDateTime);
        post.setUserPostLikes(Arrays.asList());
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
    void testGetByUnknownIdShouldReturnsErrorResponseDto() throws Exception {
        when(postService.findById(2L)).thenThrow(PostNotFoundException.class);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(postController.URL_MAPPING_POSTS + "/2");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND.name(), json.get("errorCode"));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND.getErrorNumber(), json.get("errorNumber"));
        Assertions.assertEquals(3, json.length());
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
        Assertions.assertEquals("url", json.get("filename"));
        Assertions.assertEquals("2021-10-30T16:39:00", json.get("created"));
        Assertions.assertEquals(0, json.get("likeCount"));
        Assertions.assertEquals(4, json.length());
    }

    @Test
    void testLikeUnknownPostIdShouldReturnNotFoundHttpResponseStatus() throws Exception {
        when(postService.like(anyLong(), any())).thenThrow(PostNotFoundException.class);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userPostLikeRequestDto );
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(postController.URL_MAPPING_POSTS + "/10/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson);
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testLikeUnknownUserIdShouldReturnNotFoundHttpResponseStatus() throws Exception {
        when(postService.like(anyLong(), any())).thenThrow(UserNotFoundException.class);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userPostLikeRequestDto );
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(postController.URL_MAPPING_POSTS + "/10/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testAlreadyLikedPostShouldReturnPreconditionFailedResponseStatus() throws Exception {
        when(postService.like(anyLong(), any())).thenThrow(UserAlreadyLikesPostException.class);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userPostLikeRequestDto );
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(postController.URL_MAPPING_POSTS + "/10/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isPreconditionFailed());
    }

    @Test
    void testNotLikedPostShouldReturnPreconditionFailedResponseStatus() throws Exception {
        when(postService.dislike(anyLong(), any())).thenThrow(UserDoesNotLikePostException.class);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userPostLikeRequestDto );
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(postController.URL_MAPPING_POSTS + "/10/dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isPreconditionFailed());
    }

    @Test
    void testLikeAlreadyLikedPostReturnsErrorResponseDto() throws Exception {
        when(postService.like(anyLong(), any())).thenThrow(UserAlreadyLikesPostException.class);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userPostLikeRequestDto );
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(postController.URL_MAPPING_POSTS + "/10/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("id", "10");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(ErrorCode.POST_ALREADY_LIKED.name(), json.get("errorCode"));
        Assertions.assertEquals(ErrorCode.POST_ALREADY_LIKED.getErrorNumber(), json.get("errorNumber"));
        Assertions.assertEquals(3, json.length());
    }

    @Test
    void testDislikeNotLikedPostReturnsErrorResponseDto() throws Exception {
        when(postService.dislike(anyLong(), any())).thenThrow(UserDoesNotLikePostException.class);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userPostLikeRequestDto );
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(postController.URL_MAPPING_POSTS + "/10/dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("id", "10");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(ErrorCode.POST_NOT_LIKED.name(), json.get("errorCode"));
        Assertions.assertEquals(ErrorCode.POST_NOT_LIKED.getErrorNumber(), json.get("errorNumber"));
        Assertions.assertEquals(3, json.length());
    }

    @Test
    void testDislikeUnknownUserReturnsErrorResponseDto() throws Exception {
        when(postService.dislike(anyLong(), any())).thenThrow(UserNotFoundException.class);
        UserPostLikeRequestDto userPostLikeRequestDto = new UserPostLikeRequestDto();
        userPostLikeRequestDto.setLikerId(1L);
        userPostLikeRequestDto.setOwnerId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userPostLikeRequestDto );
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(postController.URL_MAPPING_POSTS + "/10/dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("id", "10");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND.name(), json.get("errorCode"));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND.getErrorNumber(), json.get("errorNumber"));
        Assertions.assertEquals(3, json.length());
    }

    @Test
    public void whenCreatePostWithJpgPicture_thenUploadOk() throws Exception {
        when(postService.create(any(), any())).thenReturn(post);

        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "post.jpg", MediaType.IMAGE_JPEG_VALUE, "Hello, World!".getBytes());
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.multipart(postController.URL_MAPPING_POSTS)
                        .file("photo", file.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("description", "description")
                        .param("ownerId", "1");
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenCreatePostWithPngPicture_thenUploadOk() throws Exception {
        when(postService.create(any(), any())).thenReturn(post);

        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "post.jpg", MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.multipart(postController.URL_MAPPING_POSTS)
                        .file("photo", file.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("description", "description")
                        .param("ownerId", "1");

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
