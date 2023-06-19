package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CommentStatusUpdateResult {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentDtoResponse> confirmedRequests;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentDtoResponse> rejectedRequests;
}
