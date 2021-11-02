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
@Table(name = "users")
@NoArgsConstructor
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50)
    @Size(max = 50)
    private String username;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userPostLikeId.likerId")
    private List<UserPostLike> userPostLikes;

    public User(String username) {
        this.username = username;
        this.userPostLikes = new ArrayList<>();
    }
}
