package ru.practicum.api.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.admin.service.AdminCommentsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentsController {

    private final AdminCommentsService adminCommentsService;

    @DeleteMapping("/{commentId}") //затестить
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long commentId) {
        adminCommentsService.deleteComment(commentId);
    }

}
