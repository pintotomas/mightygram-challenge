package com.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 1500)
    @Size(max = 1500)
    private String description;

    @NotNull
    @Column(length = 2048)
    @Size(max = 2048)
    private String filename;

    public Post(String description, String filename) {
        this.description = description;
        this.filename = filename;
        this.userPostLikes = new ArrayList<>();
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userPostLikeId.postId")
    private List<UserPostLike> userPostLikes;
}
