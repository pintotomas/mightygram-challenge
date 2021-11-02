package com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.model.Post;
import com.utils.Dateutils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {

    @NotNull
    private Long id;

    @NotNull
    @Size(max = 1500)
    private String description;

    @NotNull
    private String filename;

    @NotNull
    private String created;

    @NotNull
    private Long likeCount;

    @NotNull
    private Long ownerId;

    public PostResponseDto(Post post, Long likeCount) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.filename = post.getFilename();
        this.created = Dateutils.toTimeStamp(post.getCreated());
        this.likeCount = likeCount;
        this.ownerId = post.getOwner().getId();
    }
}
