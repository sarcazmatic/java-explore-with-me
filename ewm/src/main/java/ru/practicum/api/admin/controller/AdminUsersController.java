package ru.practicum.api.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.admin.service.AdminUsersService;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.utility.PageableMaker;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return adminUsersService.postUser(newUserRequest);
    }

    @GetMapping
    public List<UserDto> getUsers(HttpServletRequest request,
                                 @RequestParam(required = false) List<Long> ids,
                                 @RequestParam(required = false, defaultValue = "0") int from,
                                 @RequestParam(required = false, defaultValue = "10") int size) {
        if (request.getParameter("ids") == null)
            ids = new ArrayList<>();
        return adminUsersService.getUsers(ids, PageableMaker.makePageable(from, size));
    }

    @DeleteMapping ("/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        adminUsersService.deleteUser(userId);
    }
}
