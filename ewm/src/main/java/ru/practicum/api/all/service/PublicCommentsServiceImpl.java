package ru.practicum.api.all.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDtoResponse;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCommentsServiceImpl implements PublicCommentsService {

    private final EventRepository eventRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    @Override
    public List<CommentDtoResponse> getComments(long eventId) {

        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        List<CommentDtoResponse> commentDtoResponseList = new ArrayList<>();

        for (Comment c : comments) {
            CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(c);
            commentDtoResponse.setCommenter(UserMapper.toUserShortDto(userRepository.findById(c.getCommenter().getId()).orElseThrow(
                    () -> new NotFoundException("Пользователь не найден!")
            )));
            commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(eventId).orElseThrow(
                    () -> new NotFoundException("Событие не найдено!")
            )));
            commentDtoResponseList.add(commentDtoResponse);
        }
        return commentDtoResponseList;
    }

    @Override
    public CommentDtoResponse getCommentById(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден!")
        );
        CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(comment);
        commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(comment.getEvent().getId()).orElseThrow(
                () -> new NotFoundException("Событие не найдено!")
        )));
        commentDtoResponse.setCommenter(UserMapper.toUserShortDto(userRepository.findById(comment.getCommenter().getId()).orElseThrow(
                () -> new NotFoundException("Пользователь не найден!")
        )));
        return commentDtoResponse;
    }

}
