package com.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserPostLikeRequestDto {

    @NotNull
    @Min(1)
    private Long likerId;
}
