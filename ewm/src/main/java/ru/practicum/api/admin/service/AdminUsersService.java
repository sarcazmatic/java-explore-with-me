package ru.practicum.api.admin.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface AdminUsersService {

    UserDto postUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(long userId);


}
