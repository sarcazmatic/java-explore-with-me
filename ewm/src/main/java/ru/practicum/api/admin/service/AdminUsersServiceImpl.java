package ru.practicum.api.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUsersServiceImpl implements AdminUsersService {

    private final UserRepository userRepository;

    @Override
    public UserDto postUser(NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.fromNewUserRequest(newUserRequest)));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {

        if (ids.isEmpty())
            return userRepository.findAll(pageable).stream().map(o -> UserMapper.toUserDto(o)).collect(Collectors.toList());

        return userRepository.findAllByIdIsIn(ids, pageable).stream()
                .map(o -> UserMapper.toUserDto(o))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
