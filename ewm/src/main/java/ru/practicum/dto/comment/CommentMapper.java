package ru.practicum.dto.comment;

import ru.practicum.model.Comment;
import ru.practicum.utility.CommentStatus;
import ru.practicum.utility.EWMDateTimePattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER);

    public static Comment fromNewCommentDto(NewCommentDto newCommentDto) {
        return Comment.builder().
                comment(newCommentDto.getComment())
                .commentStatus(CommentStatus.PENDING)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDtoResponse toCommentDtoResponse(Comment comment) {
        return CommentDtoResponse.builder()
                .commentStatus(comment.getCommentStatus())
                .id(comment.getId())
                .comment(comment.getComment())
                .created(comment.getCreated().format(FORMATTER))
                .build();
    }

    public static CommentDtoShort toCommentDtoShort(Comment comment) {
        return CommentDtoShort.builder()
                .commentStatus(comment.getCommentStatus())
                .id(comment.getId())
                .comment(comment.getComment())
                .created(comment.getCreated().format(FORMATTER))
                .commenterName(comment.getCommenter().getName())
                .build();
    }

}
