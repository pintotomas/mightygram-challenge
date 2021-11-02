package com.repositories;

import com.model.UserPostLike;
import com.model.compositekeys.UserPostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, UserPostLikeId> {

    //This method checks if exists a like from user with likerId belonging to postId owned by the user ownerId
    boolean existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostId
    (Long likerId, Long postId);

    //This method deletes a like from user with likerId belonging to postId owned by the user ownerId
    void deleteByUserPostLikeIdLikerIdAndUserPostLikeIdPostId
    (Long likerId, Long postId);

    //Counts how many likes from different owners a post has
    Long countByUserPostLikeIdPostIdIn
    (List<Long> postId);
}
