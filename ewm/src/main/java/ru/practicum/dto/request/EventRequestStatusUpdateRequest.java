package ru.practicum.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.utility.RequestStatus;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;
    private RequestStatus status;
}
