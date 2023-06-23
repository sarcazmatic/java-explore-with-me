package ru.practicum.api.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDtoResponse;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.comment.CommentStatusUpdateRequest;
import ru.practicum.dto.comment.CommentStatusUpdateResult;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.utility.CommentStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCommentsServiceImpl implements AdminCommentsService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteComment(long commentId) {
        commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден!")
        );

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentStatusUpdateResult patchStatusComments(CommentStatusUpdateRequest commentStatusUpdateRequest) {
        List<Comment> comments = commentRepository.findAllByIdIn(commentStatusUpdateRequest.getCommentIds());
        List<CommentDtoResponse> commentDtoResponseConfirmedList = new ArrayList<>();
        List<CommentDtoResponse> commentDtoResponseRejectedList = new ArrayList<>();
        for (Comment c : comments) {
            if (c.getCommentStatus() == commentStatusUpdateRequest.getStatus()) {
                log.error("У комментария с id {} установлен такой же статус!", c.getId());
            } else {
                c.setCommentStatus(commentStatusUpdateRequest.getStatus());
                CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(c);
                commentDtoResponse.setCommenter(UserMapper.toUserShortDto(userRepository.findById(c.getCommenter().getId()).orElseThrow(
                        () -> new NotFoundException("Пользователь не найден!")
                )));
                commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(c.getEvent().getId()).orElseThrow(
                        () -> new NotFoundException("Событие не найдено!")
                )));
                if (c.getCommentStatus() == CommentStatus.CONFIRMED) {
                    commentDtoResponseConfirmedList.add(commentDtoResponse);
                } else if (c.getCommentStatus() == CommentStatus.REJECTED) {
                    commentDtoResponseRejectedList.add(commentDtoResponse);
                }
                commentRepository.save(c);
            }
        }
        return CommentStatusUpdateResult.builder()
                .confirmedRequests(commentDtoResponseConfirmedList)
                .rejectedRequests(commentDtoResponseRejectedList)
                .build();
    }

}
