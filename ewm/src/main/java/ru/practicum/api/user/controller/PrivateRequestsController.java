package ru.practicum.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.user.service.PrivateRequestService;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestsController {

    private final PrivateRequestService privateRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postRequest(@PathVariable long userId, @RequestParam long eventId) {
        return privateRequestService.postRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId) {
        return privateRequestService.getRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patchRequestStateToCancel(@PathVariable long userId, @PathVariable long requestId) {
        return privateRequestService.patchRequestStateToCancel(userId, requestId);
    }

}
