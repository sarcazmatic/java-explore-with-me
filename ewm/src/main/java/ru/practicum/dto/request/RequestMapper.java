package ru.practicum.dto.request;

import ru.practicum.model.Request;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .requester(request.getRequester().getId())
                .id(request.getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .build();
    }

}
