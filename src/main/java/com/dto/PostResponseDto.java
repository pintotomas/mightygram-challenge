package com.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostDto {

    @NotNull
    @Size(max = 1500)
    private String description;

    @NotNull
    @Size(max = 1500)
    private String photoUrl;
}
