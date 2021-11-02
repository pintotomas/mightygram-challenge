package com.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostCreateRequestDto {

    @NotNull
    @NotEmpty
    @Size(max = 1500)
    private String description;

    @NotNull
    private Long ownerId;
}
