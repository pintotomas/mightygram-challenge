package com.dto;

import com.model.Post;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostResponseDto {

    @NotNull
    private Long id;

    @NotNull
    @Size(max = 1500)
    private String description;

    @NotNull
    @Size(max = 1500)
    private String photoUrl;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.photoUrl = post.getPhotoUrl();
    }
}
