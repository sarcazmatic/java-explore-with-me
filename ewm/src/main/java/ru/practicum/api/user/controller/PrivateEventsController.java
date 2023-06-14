package ru.practicum.api.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.user.service.PrivateEventsService;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.utility.PageableMaker;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@Slf4j
public class PrivateEventsController {

    private final PrivateEventsService privateEventsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(HttpServletRequest request,
                                   @PathVariable long userId,
                                   @RequestBody @Valid NewEventDto newEventDto) {
        return privateEventsService.postEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getEvents(HttpServletRequest request,
                                          @PathVariable long userId,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        return privateEventsService.getEvents(userId, PageableMaker.makePageable(from, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(HttpServletRequest request,
                                         @PathVariable long userId,
                                         @PathVariable long eventId) {
        return privateEventsService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequests(HttpServletRequest request,
                                                        @PathVariable long userId,
                                                        @PathVariable long eventId,
                                                        @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return privateEventsService.patchRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(HttpServletRequest request,
                                                         @PathVariable long userId,
                                                         @PathVariable long eventId,
                                                         @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return privateEventsService.patchEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForEvent(HttpServletRequest request,
                                                       @PathVariable long userId,
                                                       @PathVariable long eventId) {
        return privateEventsService.getRequestsForEvent(userId, eventId);
    }

}