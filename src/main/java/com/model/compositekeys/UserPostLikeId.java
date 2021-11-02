package com.model.compositekeys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserPostLikeId implements Serializable {

    @Column(name = "liker_id")
    private Long likerId;

    @Column(name = "post_id")
    private Long postId;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserPostLikeId))
            return false;
        if (likerId.equals(((UserPostLikeId) obj).getLikerId()) && postId.equals(((UserPostLikeId) obj).getPostId()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return likerId.hashCode() >>> postId.hashCode();
    }
}
