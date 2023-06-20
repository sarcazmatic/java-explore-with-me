package ru.practicum.api.all.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventsService {

    EventFullDto getEventById(HttpServletRequest httpServletRequest, long id);

    List<EventFullDto> getEventsFiltered(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         Pageable pageable,
                                         HttpServletRequest httpServletRequest);

}
