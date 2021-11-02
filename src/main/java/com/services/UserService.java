package com.services;

import com.dto.UserParenthoodRequestDto;
import com.exceptions.UserAlreadyHasAParentException;
import com.exceptions.UserCantParentSelfException;
import com.exceptions.UserNotFoundException;
import com.model.User;
import com.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("User {} not found", id);
                    throw new UserNotFoundException("User not found with id " + id);
                }
        );
    }

    public User assignParent(UserParenthoodRequestDto userParenthoodRequestDto) {

        // Assigns the child a parent
        // If both are equal, throws UserCantParentSelfException
        // If child already had a parent, throws UserAlreadyHasAParentException
        // If any of the user is not found, throws UserNotFoundException

        Long childId = userParenthoodRequestDto.getChildId();
        Long parentId = userParenthoodRequestDto.getParentId();
        if (childId.equals(parentId)) {
            log.error("User {} attempted to parent self", parentId);
            throw new UserCantParentSelfException("User " + childId + " attempted to parent self");
        }
        User child = this.findById(childId);
        if (child.getParent().isPresent()) {
            log.error("User {} already has a parent", parentId);
            throw new UserAlreadyHasAParentException("User " + childId + " has already a parent assigned");
        }
        User parent = this.findById(parentId);
        child.setParent(parent);
        return userRepository.save(child);
    }

    public Collection<User> findAllChildren(User parent) {
        return userRepository.findAllByParent(parent);
    }
}
