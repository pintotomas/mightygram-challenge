package com.model;

import com.model.compositekeys.UserPostLikeId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_post_likes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPostLike implements Serializable {
    // This entity represents when a user likes a post
    // I used a separated entity because the relationship
    // could have state like the time of like, or a comment

    @EmbeddedId
    protected UserPostLikeId userPostLikeId;

}
