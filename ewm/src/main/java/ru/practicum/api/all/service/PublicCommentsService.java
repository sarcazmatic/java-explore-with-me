package ru.practicum.api.all.service;

import ru.practicum.dto.comment.CommentDtoResponse;

import java.util.List;

public interface PublicCommentsService {

    List<CommentDtoResponse> getComments(long eventId);

    CommentDtoResponse getCommentById(long commentId);

}
