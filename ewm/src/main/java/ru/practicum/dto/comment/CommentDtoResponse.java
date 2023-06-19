package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.utility.CommentStatus;

@Data
@Builder
public class CommentDtoResponse {

    private long id;
    private String comment;
    private String created;
    private CommentStatus commentStatus;
    private EventShortDto event;
    private UserShortDto commenter;

}
