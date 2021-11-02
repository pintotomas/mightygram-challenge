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
import java.util.*;

@Service
@Slf4j
public class PostService {

    @Autowired private PostRepository postRepository;

    @Autowired private UserPostLikeRepository userPostLikeRepository;

    @Autowired private UserService userService;

    @Autowired private StorageService storageService;

    public Post findByIdAndOwner(Long id, Long ownerId) {
        return postRepository.findByIdAndOwnerId(id, ownerId).orElseThrow(
                () -> new PostNotFoundException("Post with id " + id + " not found " + " for owner id " + ownerId)
        );
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("Post with id " + id + " not found")
        );
    }

    public Page<Post> findAllPaginated(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("created").descending());
        return postRepository.findAll(pageRequest);
    }

    //Annotate as transactional so we don't have to add post.addUserPostLike()
    @Transactional
    public Post like(Long postId, UserPostLikeRequestDto userPostLikeRequestDto) {
        User owner = userService.findById(userPostLikeRequestDto.getOwnerId());
        Post post = this.findByIdAndOwner(postId, owner.getId());
        User liker = userService.findById(userPostLikeRequestDto.getLikerId());
        if (userPostLikeRepository.existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId
                (liker.getId(), postId, owner.getId())) {
            log.error
                    ("User {} has already liked post {} from user {}",
                            userPostLikeRequestDto.getLikerId(), postId, userPostLikeRequestDto.getOwnerId());
            throw new UserAlreadyLikesPostException("User " + userPostLikeRequestDto.getLikerId()
                    + " has already liked post " + postId + " from the owner " + owner.getId());
        }

        userPostLikeRepository.save(new UserPostLike(
                new UserPostLikeId(liker.getId(), post.getId(), owner.getId())
        ));
        return postRepository.save(post);
    }

    public Long likeCount(Post post, User owner) {
        Optional<User> parent = owner.getParent();
        ArrayList<Long> userIds = new ArrayList<>();
        userIds.add(owner.getId());
        if (parent.isPresent()) {
            userIds.add(parent.get().getId());
        }
        ArrayList<Long> postIds = new ArrayList<>();
        postIds.add(post.getId());
        Optional<Post> parentPost = post.getParentPost();
        if (parentPost.isPresent()) {
            postIds.add(parentPost.get().getId());
        }
        return userPostLikeRepository.countByUserPostLikeIdPostIdInAndUserPostLikeIdOwnerIdIn(postIds, userIds);
    }

    //Annotate as transactional so we don't have to add post.removeUserPostLike()
    @Transactional
    public Post dislike(Long postId, UserPostLikeRequestDto userPostLikeRequestDto) {
        if (!userPostLikeRepository.existsByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId
                (userPostLikeRequestDto.getLikerId(), postId, userPostLikeRequestDto.getOwnerId())) {
            log.error
                    ("User {} has not liked post {} from owner {}",
                            userPostLikeRequestDto.getLikerId(), postId, userPostLikeRequestDto.getOwnerId());
            throw new UserDoesNotLikePostException("User " + userPostLikeRequestDto.getLikerId()
                    + " does not like post " + postId + " from owner " + userPostLikeRequestDto.getOwnerId());
        }

        userPostLikeRepository.deleteByUserPostLikeIdLikerIdAndUserPostLikeIdPostIdAndUserPostLikeIdOwnerId
                (userPostLikeRequestDto.getLikerId(), postId, userPostLikeRequestDto.getOwnerId());
        return postRepository.getById(postId);
    }

    @Transactional
    public Post create(PostCreateRequestDto postCreateRequestDto, MultipartFile photo) {
        //TODO if rollback, delete file
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
        return post;
    }
}
