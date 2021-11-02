package com.services;

import com.exceptions.UserNotFoundException;
import com.model.User;
import com.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
