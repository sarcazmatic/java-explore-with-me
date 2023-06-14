package ru.practicum.dto.user;

import ru.practicum.model.User;

public class UserMapper {

    public static User fromNewUserRequest(NewUserRequest newUserRequest){
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user){
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }

    public static UserShortDto toUserShortDto(User user){
        return UserShortDto.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }

}
