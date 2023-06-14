package ru.practicum.api.admin.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.utility.EventState;
import ru.practicum.utility.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventsServiceImpl implements AdminEventsService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    public List<EventFullDto> getEventsAdmin(List<Long> users,
                                             List<EventState> states,
                                             List<Long> categories,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             Pageable pageable) {

        List<Event> events = eventRepository.findAllByParams(users,
                states,
                categories,
                pageable);

        List<EventFullDto> efdList;

        if (Optional.ofNullable(rangeStart).isPresent() && Optional.ofNullable(rangeEnd).isPresent()) {
            efdList = events.stream()
                    .filter(o -> o.getEventDate().isAfter(rangeStart.minusNanos(1)))
                    .filter(o -> o.getEventDate().isBefore(rangeEnd.plusNanos(1)))
                    .map(e -> EventMapper.toEventFullDto(e)).collect(Collectors.toList());
        } else if (Optional.ofNullable(rangeStart).isPresent()) {
            efdList = events.stream()
                    .filter(o -> o.getEventDate().isAfter(rangeStart.minusNanos(1)))
                    .map(e -> EventMapper.toEventFullDto(e)).collect(Collectors.toList());
        } else if (Optional.ofNullable(rangeEnd).isPresent()) {
            efdList = events.stream()
                    .filter(o -> o.getEventDate().isBefore(rangeEnd.plusNanos(1)))
                    .map(e -> EventMapper.toEventFullDto(e)).collect(Collectors.toList());
        } else {
            efdList = events.stream()
                    .map(e -> EventMapper.toEventFullDto(e)).collect(Collectors.toList());
        }
        for (EventFullDto efd : efdList) {
            efd.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(efd.getId(), RequestStatus.CONFIRMED));
        }
        return efdList;
    }

    public EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = EventMapper.updateEventAdminRequest(eventRepository.findById(eventId)
                .orElseThrow(()
                        -> new NotFoundException("Событие не найдено!")), updateEventAdminRequest);

        if (updateEventAdminRequest.getCategory() > 0)
            event.setCategory(categoryRepository.findCategoryById(updateEventAdminRequest.getCategory()));

        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        return eventFullDto;
    }

}