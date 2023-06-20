package ru.practicum.api.admin.service;


import ru.practicum.dto.comment.CommentStatusUpdateRequest;
import ru.practicum.dto.comment.CommentStatusUpdateResult;

public interface AdminCommentsService {

    void deleteComment(long commentId);

    CommentStatusUpdateResult patchStatusComments(CommentStatusUpdateRequest commentStatusUpdateRequest);

}
