package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.utility.CommentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Data
public class CommentStatusUpdateRequest {

    @NotEmpty
    private List<Long> commentIds;
    @NotBlank
    private CommentStatus status;

}
