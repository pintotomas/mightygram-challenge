package com.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_post_likes")
public class UserPostLike implements Serializable {
    // This entity represents when a user likes a post
    // I used a separated entity because the relationship
    // could have state like the time of like, or a comment

    @ManyToOne
    @Id
    private User user;

    @ManyToOne
    @Id
    private Post post;
}
