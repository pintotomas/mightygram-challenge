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
import com.model.events.PostCreatedEvent;
import com.repositories.PostRepository;
import com.repositories.UserPostLikeRepository;
import com.services.storage.StorageService;
import com.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class PostService {

    @Autowired private PostRepository postRepository;

    @Autowired private UserPostLikeRepository userPostLikeRepository;

    @Autowired private UserService userService;

    @Autowired private StorageService storageService;

    @Autowired private ApplicationEventPublisher applicationEventPublisher;

    public Post findByIdAndOwner(Long id, Long ownerId) {
        return postRepository.findByIdAndOwnerId(id, ownerId).orElseThrow(
                () -> {
                    log.error("Post {} with owner {} not found", id, ownerId);
                    throw new PostNotFoundException("Post with id " + id + " not found " + " for owner id " + ownerId);
                }
        );
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Post {} not found", id);
                    throw new PostNotFoundException("Post with id " + id + " not found");
                }
        );
    }

    public Page<Post> findAllPaginated(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("created").descending());
        return postRepository.findAll(pageRequest);
    }

    //Annotate as transactional so we don't have to add post.addUserPostLike()
    @Transactional
    public Post like(Long postId, UserPostLikeRequestDto userPostLikeRequestDto) {

        // Gives a like from the likerId in userPostLikeRequestDto to
        // the Post postId with owner ownerId in userPostLikeRequestDto
        // Throws:
        // UserNotFoundException if any of the users are not found
        // UserAlreadyLikesPostException if the like already exists

        Post post = this.findById(postId);
        User liker = userService.findById(userPostLikeRequestDto.getLikerId());
        if (userPostLikeRepository.existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostId
                (liker.getId(), postId)) {
            log.error
                    ("User {} has already liked post {} from user {}",
                            userPostLikeRequestDto.getLikerId(), postId);
            throw new UserAlreadyLikesPostException("User " + userPostLikeRequestDto.getLikerId()
                    + " has already liked post " + postId + " from the owner ");
        }

        userPostLikeRepository.save(new UserPostLike(
                new UserPostLikeId(liker.getId(), post.getId())
        ));
        return postRepository.save(post);
    }

    public Long likeCount(Post post) {

        // Counts all the likes the owner of the post has
        // Also, if this post was inherited from the owner parent,
        // the likeCount from the parent post is added up

        ArrayList<Long> postIds = new ArrayList<>();
        postIds.add(post.getId());
        Optional<Post> parentPost = post.getParentPost();
        if (parentPost.isPresent()) {
            postIds.add(parentPost.get().getId());
        }
        return userPostLikeRepository.countByUserPostLikeIdPostIdIn(postIds);
    }

    //Annotate as transactional so we don't have to add post.removeUserPostLike()
    @Transactional
    public Post dislike(Long postId, UserPostLikeRequestDto userPostLikeRequestDto) {

        // Gives a dislike from the likerId in userPostLikeRequestDto to
        // the Post postId with owner ownerId in userPostLikeRequestDto
        // Throws:
        // UserNotFoundException if any of the users are not found
        // UserDoesNotLikePostException if the user likerId didnt like the post before

        if (!userPostLikeRepository.existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostId
                (userPostLikeRequestDto.getLikerId(), postId)) {
            log.error
                    ("User {} has not liked post {} from owner {}",
                            userPostLikeRequestDto.getLikerId(), postId);
            throw new UserDoesNotLikePostException("User " + userPostLikeRequestDto.getLikerId()
                    + " does not like post " + postId);
        }

        userPostLikeRepository.deleteByUserPostLikeIdLikerIdAndUserPostLikeIdPostId
                (userPostLikeRequestDto.getLikerId(), postId);
        return postRepository.getById(postId);
    }

    @Transactional
    public Post create(PostCreateRequestDto postCreateRequestDto, MultipartFile photo) {

        // Creates a new post with the photo and the description specified. The owner of the post
        // will be the one specified in postCreateRequestDto. Additionally if the user has adopted other users,
        // for each one of them a Post will be created with them being the owners of each post.
        // Throws:
        // UserNotFoundException if any of the users are not found
        // StorageException if there is any problem with the persistence of the photo
        // If after the persistence of the photo an error occurs in the db, an event is raised to
        // delete the saved photo.

        User parent = userService.findById(postCreateRequestDto.getOwnerId());
        String fileName = FileUtils.generateFileName(photo.getName());
        storageService.store(photo, fileName);
        Post post = new Post(postCreateRequestDto.getDescription(), fileName, parent, null);
        Collection<User> ownerChildren = userService.findAllChildren(parent);
        post = postRepository.save(post);
        Post finalPost = post;
        ownerChildren.forEach(child ->
            postRepository.save(new Post(postCreateRequestDto.getDescription(), fileName, child, finalPost))
        );
        applicationEventPublisher.publishEvent(new PostCreatedEvent(fileName));
        return post;
    }
}
