package com.services;

import com.exceptions.UserNotFoundException;
import com.model.User;
import com.repositories.UserPostLikeRepository;
import com.repositories.UserRepository;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Rule
    private MockitoRule rule = MockitoJUnit.rule();

    @Test
    void testFindByIdSuccess() {
        User user = new User("username");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User userFound = userService.findById(1L);
        Assertions.assertEquals("username", userFound.getUsername());
        Assertions.assertEquals(1L, userFound.getId());
    }

    @Test
    void testFindByIdThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

}
