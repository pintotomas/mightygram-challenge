package com.services;

import com.dto.PostCreateRequestDto;
import com.dto.UserPostLikeRequestDto;
import com.exceptions.PostNotFoundException;
import com.exceptions.UserAlreadyLikesPostException;
import com.exceptions.UserDoesNotLikePostException;
import com.model.Post;
import com.model.User;
import com.model.UserPostLike;
import com.model.compositekeys.UserPostLikeId;
import com.repositories.PostRepository;
import com.repositories.UserPostLikeRepository;
import com.services.storage.StorageService;
import com.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@Slf4j
public class PostService {

    @Autowired private PostRepository postRepository;

    @Autowired private UserPostLikeRepository userPostLikeRepository;

    @Autowired private UserService userService;

    @Autowired private StorageService storageService;

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("Post with id" + id + "not found")
        );
    }

    public Page<Post> findAllPaginated(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("created").descending());
        return postRepository.findAll(pageRequest);
    }

    //Annotate as transactional so we don't have to add post.addUserPostLike()
    @Transactional
    public Post like(Long postId, UserPostLikeRequestDto userPostLikeRequestDto) {
        Post post = this.findById(postId);
        User user = userService.findById(userPostLikeRequestDto.getUserId());
        if (userPostLikeRepository.existsByUserPostLikeIdUserIdAndUserPostLikeIdPostId
                (userPostLikeRequestDto.getUserId(), postId)) {
            log.error
                    ("User {} has already liked post {}",
                            userPostLikeRequestDto.getUserId(), postId);
            throw new UserAlreadyLikesPostException("User " + userPostLikeRequestDto.getUserId()
                    + " has already liked post " + postId);
        }

        userPostLikeRepository.save(new UserPostLike(
                new UserPostLikeId(user.getId(), post.getId())
        ));
        return postRepository.save(post);
    }

    //Annotate as transactional so we don't have to add post.removeUserPostLike()
    @Transactional
    public Post dislike(Long postId, UserPostLikeRequestDto userPostLikeRequestDto) {
        if (!userPostLikeRepository.existsByUserPostLikeIdUserIdAndUserPostLikeIdPostId
                (userPostLikeRequestDto.getUserId(), postId)) {
            log.error
                    ("User {} has not liked post {}",
                            userPostLikeRequestDto.getUserId(), postId);
            throw new UserDoesNotLikePostException("User " + userPostLikeRequestDto.getUserId()
                    + " does not like post " + postId);
        }

        userPostLikeRepository.deleteByUserPostLikeIdUserIdAndUserPostLikeIdPostId
                (userPostLikeRequestDto.getUserId(), postId);
        return postRepository.getById(postId);
    }

    public Post create(PostCreateRequestDto postCreateRequestDto, MultipartFile photo) {
        String fileName = FileUtils.generateFileName(photo.getName());
        storageService.store(photo, fileName);
        Post post = new Post(postCreateRequestDto.getDescription(), fileName);
        return postRepository.save(post);
    }
}
