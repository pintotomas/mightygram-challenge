package com.dto;

import com.model.Post;
import com.utils.Dateutils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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

    @NotNull
    private String created;

    //TODO could add time since creation

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.photoUrl = post.getPhotoUrl();
        this.created = Dateutils.toTimeStamp(post.getCreated());
    }
}
