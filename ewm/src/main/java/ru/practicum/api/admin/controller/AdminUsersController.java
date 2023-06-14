package ru.practicum.api.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(HttpServletRequest request, @RequestBody @Valid NewUserRequest newUserRequest) {
        System.out.println(newUserRequest.getName());
        return adminUsersService.postUser(newUserRequest);
    }

    @GetMapping()
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
    public void deleteUser(HttpServletRequest request, @PathVariable long userId) {
        adminUsersService.deleteUser(userId);
    }
}
