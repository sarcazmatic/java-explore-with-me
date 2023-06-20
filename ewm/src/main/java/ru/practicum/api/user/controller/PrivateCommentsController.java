package ru.practicum.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.user.service.PrivateCommentsService;
import ru.practicum.dto.comment.CommentDtoResponse;
import ru.practicum.dto.comment.CommentStatusUpdateRequest;
import ru.practicum.dto.comment.CommentStatusUpdateResult;
import ru.practicum.dto.comment.NewCommentDto;

import javax.validation.Valid;
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

    @PatchMapping("/{commentId}/cancel")
    public CommentDtoResponse patchCancelComment(@PathVariable long userId,
                                                 @PathVariable long commentId) {
        return privateCommentsService.patchCancelComment(userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId, @PathVariable long commentId) {
        privateCommentsService.deleteComment(userId, commentId);
    }

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse postNewComment(@PathVariable long userId,
                                             @PathVariable long eventId,
                                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return privateCommentsService.postNewComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/events/{eventId}")
    public CommentStatusUpdateResult patchStatusComments(@PathVariable long userId,
                                                         @PathVariable long eventId,
                                                         @RequestBody CommentStatusUpdateRequest commentStatusUpdateRequest) {
        return privateCommentsService.patchStatusComments(userId, eventId, commentStatusUpdateRequest);
    }

}
