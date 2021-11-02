package com.repositories;

import com.model.UserPostLike;
import com.model.compositekeys.UserPostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, UserPostLikeId> {

    boolean existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostId(Long userId, Long postId);

    void deleteByUserPostLikeIdLikerIdAndUserPostLikeIdPostId(Long userId, Long postId);
}
