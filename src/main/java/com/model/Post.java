package com.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Column(length = 1500)
    @Size(max = 1500)
    private String photoUrl;

    public Post(String description, String photoUrl) {
        this.description = description;
        this.photoUrl = photoUrl;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<UserPostLike> userPostLikes;
}
