package ru.practicum.api.user.service;

import ru.practicum.dto.comment.CommentDtoResponse;

import java.util.List;

public interface PrivateCommentsService {

    List<CommentDtoResponse> getCommentByUserId(long userId);


    CommentDtoResponse patchCancelComment(long userId, long commentId);

    void deleteComment(long userId, long commentId);

}
