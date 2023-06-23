package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.utility.CommentStatus;

@Data
@Builder
public class CommentDtoShort {

    private long id;
    private String comment;
    private String created;
    private CommentStatus commentStatus;
    private String commenterName;

}
