package com.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50)
    @Size(max = 50)
    private String username;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserPostLike> userPostLikes;
}
