package ru.practicum.api.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.admin.service.AdminEventsService;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.utility.EWMDateTimePattern;
import ru.practicum.utility.EventState;
import ru.practicum.utility.PageableMaker;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Slf4j
public class AdminEventsController {

    private final AdminEventsService adminEventsService;


    @GetMapping
    public List<EventFullDto> getEventsAdmin(HttpServletRequest httpServletRequest,
                                             @RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<EventState> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = EWMDateTimePattern.FORMATTER) LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = EWMDateTimePattern.FORMATTER) LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return adminEventsService.getEventsAdmin(users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                PageableMaker.makePageable(from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEventAdmin(HttpServletRequest httpServletRequest,
                                        @PathVariable long eventId,
                                        @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return adminEventsService.patchEventAdmin(eventId, updateEventAdminRequest);
    }


}
