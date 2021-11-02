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
    private Integer likeCount;

    @NotNull
    private Long ownerId;

    //TODO could add time since creation

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.filename = post.getFilename();
        this.created = Dateutils.toTimeStamp(post.getCreated());
        this.likeCount = post.getUserPostLikes().size();
        this.ownerId = post.getOwner().getId();
    }
}
