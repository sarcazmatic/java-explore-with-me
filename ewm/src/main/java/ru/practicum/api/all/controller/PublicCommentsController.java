package ru.practicum.api.all.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.all.service.PublicCommentsService;
import ru.practicum.dto.comment.CommentDtoResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentsController {

    private final PublicCommentsService publicCommentsService;

    @GetMapping("/events/{eventId}")
    public List<CommentDtoResponse> getComments(@PathVariable long eventId) {
        return publicCommentsService.getComments(eventId);
    }

    @GetMapping("/{commentId}")
    public CommentDtoResponse getCommentById(@PathVariable long commentId) {
        return publicCommentsService.getCommentById(commentId);
    }
}
