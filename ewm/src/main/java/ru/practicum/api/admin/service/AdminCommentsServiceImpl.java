package ru.practicum.api.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class AdminCommentsServiceImpl implements AdminCommentsService {

    private final CommentRepository commentRepository;

    @Override
    public void deleteComment(long commentId) {
        commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден!")
        );

        commentRepository.deleteById(commentId);
    }

}
