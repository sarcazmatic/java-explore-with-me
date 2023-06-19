package ru.practicum.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.user.service.PrivateEventsService;
import ru.practicum.dto.comment.CommentDtoResponse;
import ru.practicum.dto.comment.CommentStatusUpdateRequest;
import ru.practicum.dto.comment.CommentStatusUpdateResult;
import ru.practicum.dto.comment.NewCommentDto;
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
public class PrivateEventsController {

    private final PrivateEventsService privateEventsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return privateEventsService.postEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                         @RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size,
                                         HttpServletRequest httpServletRequest) {
        return privateEventsService.getEvents(userId, PageableMaker.makePageable(from, size), httpServletRequest);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable long userId,
                                     @PathVariable long eventId,
                                     HttpServletRequest httpServletRequest) {
        return privateEventsService.getEventById(userId, eventId, httpServletRequest);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequests(@PathVariable long userId,
                                                        @PathVariable long eventId,
                                                        @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                        HttpServletRequest httpServletRequest) {
        return privateEventsService.patchRequests(userId, eventId, eventRequestStatusUpdateRequest, httpServletRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(HttpServletRequest httpServletRequest,
                                   @PathVariable long userId,
                                   @PathVariable long eventId,
                                   @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return privateEventsService.patchEvent(userId, eventId, updateEventUserRequest, httpServletRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable long userId, @PathVariable long eventId) {
        return privateEventsService.getRequestsForEvent(userId, eventId);
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse postNewComment(@PathVariable long userId,
                                             @PathVariable long eventId,
                                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return privateEventsService.postNewComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{eventId}/comments")
    public CommentStatusUpdateResult patchStatusComments(@PathVariable long userId,
                                                         @PathVariable long eventId,
                                                         @RequestBody CommentStatusUpdateRequest commentStatusUpdateRequest) {
        return privateEventsService.patchStatusComments(userId, eventId, commentStatusUpdateRequest);
    }

}
