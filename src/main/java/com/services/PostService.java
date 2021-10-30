package com.services;

import com.exceptions.PostNotFoundException;
import com.model.Post;
import com.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PostService {

    @Autowired private PostRepository postRepository;

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("Post with id" + id + "not found")
        );
    }
}
