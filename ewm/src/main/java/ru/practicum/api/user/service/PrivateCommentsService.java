package ru.practicum.api.user.service;

import ru.practicum.dto.comment.CommentDtoResponse;
import ru.practicum.dto.comment.CommentStatusUpdateRequest;
import ru.practicum.dto.comment.CommentStatusUpdateResult;
import ru.practicum.dto.comment.NewCommentDto;

import java.util.List;

public interface PrivateCommentsService {

    List<CommentDtoResponse> getCommentByUserId(long userId);


    CommentDtoResponse patchCancelComment(long userId, long commentId);

    void deleteComment(long userId, long commentId);

    CommentDtoResponse postNewComment(long userId, long eventId, NewCommentDto newCommentDto);

    CommentStatusUpdateResult patchStatusComments(long userId,
                                                  long eventId,
                                                  CommentStatusUpdateRequest commentStatusUpdateRequest);

}
