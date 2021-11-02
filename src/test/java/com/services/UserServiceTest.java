package com.services;

import com.dto.UserParenthoodRequestDto;
import com.exceptions.UserAlreadyHasAParentException;
import com.exceptions.UserCantParentSelfException;
import com.exceptions.UserNotFoundException;
import com.model.User;
import com.repositories.UserRepository;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

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

    @Test
    void testAssignParentWhenChildAlreadyHasAParentThrowsException() {
        User child = new User("child");
        child.setId(1L);

        User parent = new User("parent");
        parent.setId(2L);
        child.setParent(parent);

        User newParent = new User("newParent");
        newParent.setId(3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(child));
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(3L);
        userParenthoodRequestDto.setChildId(1L);
        Assertions.assertThrows(UserAlreadyHasAParentException.class,
                () -> userService.assignParent(userParenthoodRequestDto));
    }

    @Test
    void testAssignSelfAsParentThrowsException() {
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(1L);
        userParenthoodRequestDto.setChildId(1L);
        Assertions.assertThrows(UserCantParentSelfException.class,
                () -> userService.assignParent(userParenthoodRequestDto));
    }

    @Test
    void testAssignParentSuccess() {
        UserParenthoodRequestDto userParenthoodRequestDto = new UserParenthoodRequestDto();
        userParenthoodRequestDto.setParentId(2L);
        userParenthoodRequestDto.setChildId(1L);
        User child = new User("child");
        child.setId(1L);

        User parent = new User("parent");
        parent.setId(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(child));
        when(userRepository.findById(2L)).thenReturn(Optional.of(parent));
        userService.assignParent(userParenthoodRequestDto);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        Assertions.assertEquals(parent.getUsername(), userArgumentCaptor.getValue().getParent().get().getUsername());
    }

}
