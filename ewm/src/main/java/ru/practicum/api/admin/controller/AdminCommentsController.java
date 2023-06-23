package ru.practicum.api.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.admin.service.AdminCommentsService;
import ru.practicum.dto.comment.CommentStatusUpdateRequest;
import ru.practicum.dto.comment.CommentStatusUpdateResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentsController {

    private final AdminCommentsService adminCommentsService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long commentId) {
        adminCommentsService.deleteComment(commentId);
    }

    @PatchMapping
    public CommentStatusUpdateResult patchStatusComments(@RequestBody CommentStatusUpdateRequest commentStatusUpdateRequest) {
        return adminCommentsService.patchStatusComments(commentStatusUpdateRequest);
    }

}
