package ru.practicum.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.user.service.PrivateCommentsService;
import ru.practicum.dto.comment.CommentDtoResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentsController {

    private final PrivateCommentsService privateCommentsService;

    @GetMapping
    public List<CommentDtoResponse> getCommentByUserId(@PathVariable long userId) {
        return privateCommentsService.getCommentByUserId(userId);
    }

    @PatchMapping("/{commentId}/cancel") //затестить
    public CommentDtoResponse patchCancelComment(@PathVariable long userId,
                                                 @PathVariable long commentId) {
        return privateCommentsService.patchCancelComment(userId, commentId);
    }

    @DeleteMapping("/{commentId}") //затестить
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId, @PathVariable long commentId) {
        privateCommentsService.deleteComment(userId, commentId);
    }

}
