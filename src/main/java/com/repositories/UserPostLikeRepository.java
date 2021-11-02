package com.repositories;

import com.model.UserPostLike;
import com.model.compositekeys.UserPostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, UserPostLikeId> {

    //This method checks if there is a like from user with likerId belonging to postId owned by the user ownerId
    boolean existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId(Long likerId, Long postId, Long ownerId);

    //This method deletes a like from user with likerId belonging to postId owned by the user ownerId
    void deleteByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId(Long likerId, Long postId, Long ownerId);
}
