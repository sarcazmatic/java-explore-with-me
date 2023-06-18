package ru.practicum.api.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;


public interface PrivateEventsService {

    EventFullDto postEvent(long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(long userId, Pageable pageable);

    EventFullDto getEventById(long userId, long eventId);

    EventRequestStatusUpdateResult patchRequests(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    EventFullDto patchEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getRequestsForEvent(long userId, long eventId);

}
