package ru.practicum.api.admin.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.utility.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsService {

    List<EventFullDto> getEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable, HttpServletRequest httpServletRequest);

    EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest httpServletRequest);

}
