package com.dto;

import com.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
@Setter
public class UserResponseDto {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String username;

    private UserResponseDto parent;


    public UserResponseDto(User user, Boolean includeParent) {
        this.id = user.getId();
        this.username = user.getUsername();
        Optional<User> optionalUser = user.getParent();
        if (includeParent && optionalUser.isPresent()) {
            this.parent = new UserResponseDto(optionalUser.get(), false);
        }

    }
}
