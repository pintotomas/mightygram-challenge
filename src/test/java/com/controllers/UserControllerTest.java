package com.controllers;

import com.advisors.UserControllerAdvisor;
import com.dto.UserParenthoodRequestDto;
import com.exceptions.ErrorCode;
import com.exceptions.UserAlreadyHasAParentException;
import com.exceptions.UserCantParentSelfException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.services.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new UserControllerAdvisor())
                .build();
    }

    @Test
    void testParentSelfReturnsMethodNotAllowedHttpStatusCode() throws Exception {
        when(userService.assignParent(any())).thenThrow(UserCantParentSelfException.class);
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userParenthoodRequestDto);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(userController.URL_MAPPING_POSTS + "/parenthood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("id", "10");
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    void testParentChildWithParentAlreadyAssignedReturnsPreconditionFailedHttpStatusCode() throws Exception {
        when(userService.assignParent(any())).thenThrow(UserAlreadyHasAParentException.class);
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userParenthoodRequestDto);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(userController.URL_MAPPING_POSTS + "/parenthood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("id", "10");
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isPreconditionFailed());
    }

    @Test
    void testParentSelfReturnsErrorResponseDto() throws Exception {
        when(userService.assignParent(any())).thenThrow(UserCantParentSelfException.class);
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userParenthoodRequestDto);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(userController.URL_MAPPING_POSTS + "/parenthood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("id", "10");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(ErrorCode.USER_CANT_PARENT_SELF.name(), json.get("errorCode"));
        Assertions.assertEquals(ErrorCode.USER_CANT_PARENT_SELF.getErrorNumber(), json.get("errorNumber"));
        Assertions.assertEquals(3, json.length());
    }

    @Test
    void testParentChildWithParentAlreadyAssignedReturnsErrorResponseDto() throws Exception {
        when(userService.assignParent(any())).thenThrow(UserAlreadyHasAParentException.class);
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(2L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userParenthoodRequestDto);
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(userController.URL_MAPPING_POSTS + "/parenthood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("id", "10");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(ErrorCode.USER_ALREADY_HAS_PARENT.name(), json.get("errorCode"));
        Assertions.assertEquals(ErrorCode.USER_ALREADY_HAS_PARENT.getErrorNumber(), json.get("errorNumber"));
        Assertions.assertEquals(3, json.length());
    }
}
