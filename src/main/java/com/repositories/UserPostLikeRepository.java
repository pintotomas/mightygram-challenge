package com.repositories;

import com.model.UserPostLike;
import com.model.compositekeys.UserPostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, UserPostLikeId> {

    boolean existsByUserPostLikeIdUserIdAndUserPostLikeIdPostId(Long userId, Long postId);

    void deleteByUserPostLikeIdUserIdAndUserPostLikeIdPostId(Long userId, Long postId);
}
