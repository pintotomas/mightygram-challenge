package com.controllers;

import com.dto.UserResponseDto;
import com.dto.UserParenthoodRequestDto;
import com.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(UserController.URL_MAPPING_POSTS)
@Validated
public class UserController {

    public static final String URL_MAPPING_POSTS = "/users";

    @Autowired private UserService userService;

    @PostMapping("/parenthood")
    @ResponseBody
    public ResponseEntity<UserResponseDto> parenthood(
            @RequestBody @Validated UserParenthoodRequestDto userParenthoodRequestDto
    ) {
        log.info("Parenthood requested from user {} to user {}",
                userParenthoodRequestDto.getParentId(), userParenthoodRequestDto.getChildId());
        return ResponseEntity.ok(
                new UserResponseDto(userService.assignParent(userParenthoodRequestDto), true));
    }

}
