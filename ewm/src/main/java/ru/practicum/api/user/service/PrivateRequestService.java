package ru.practicum.api.user.service;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;


public interface PrivateRequestService {

    ParticipationRequestDto postRequest(long userId, long eventId);

    List<ParticipationRequestDto> getRequests(long userId);

    ParticipationRequestDto patchRequestStateToCancel(long userId, long requestId);

}
