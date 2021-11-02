package com.repositories;

import com.model.Post;
import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndOwnerId(Long idPost, Long idOwner);
}
