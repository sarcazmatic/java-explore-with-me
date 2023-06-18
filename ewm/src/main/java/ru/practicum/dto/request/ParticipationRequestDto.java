package ru.practicum.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.utility.RequestStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class ParticipationRequestDto {

    private long id;
    private LocalDateTime created;
    private long event;
    private long requester;
    private RequestStatus status;
}
