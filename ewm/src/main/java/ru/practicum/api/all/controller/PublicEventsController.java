package ru.practicum.api.all.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.all.service.PublicEventsService;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.utility.EWMDateTimePattern;
import ru.practicum.utility.PageableMaker;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventsController {

    private final PublicEventsService publicEventsService;

    @GetMapping("/{id}")
    public EventFullDto getEventById(HttpServletRequest httpServletRequest, @PathVariable long id) {
        return publicEventsService.getEventById(httpServletRequest, id);
    }

    @GetMapping
    public List<EventFullDto> getEventsFiltered(HttpServletRequest httpServletRequest,
                                                @RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false)
                                                    @DateTimeFormat(pattern = EWMDateTimePattern.FORMATTER) LocalDateTime rangeStart,
                                                @RequestParam(required = false)
                                                    @DateTimeFormat(pattern = EWMDateTimePattern.FORMATTER) LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        if (httpServletRequest.getParameter("rangeStart") == null && httpServletRequest.getParameter("rangeEnd") == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = LocalDateTime.now().plusYears(10);
        }

        if (httpServletRequest.getParameter("rangeStart") == null)
            rangeStart = LocalDateTime.now().minusYears(10);
        if (httpServletRequest.getParameter("rangeEnd") == null)
            rangeEnd = LocalDateTime.now().plusYears(10);

        if (rangeEnd.isBefore(LocalDateTime.now()))
            throw new ValidationException("Дата события не может быть в прошлом");

        return publicEventsService.getEventsFiltered(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                PageableMaker.makePageable(from, size), httpServletRequest);
    }

}
