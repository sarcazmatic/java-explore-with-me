package ru.practicum.api.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.user.service.PrivateRequestService;
import ru.practicum.dto.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@Slf4j
public class PrivateRequestsController {

    private final PrivateRequestService privateRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postRequest(HttpServletRequest httpServletRequest,
                                               @PathVariable long userId,
                                               @RequestParam long eventId) {
        return privateRequestService.postRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequests(HttpServletRequest httpServletRequest,
                                                     @PathVariable long userId) {
        return privateRequestService.getRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patchRequestStateToCancel(HttpServletRequest httpServletRequest,
                                                             @PathVariable long userId,
                                                             @PathVariable long requestId) {
        return privateRequestService.patchRequestStateToCancel(userId, requestId);
    }

}
