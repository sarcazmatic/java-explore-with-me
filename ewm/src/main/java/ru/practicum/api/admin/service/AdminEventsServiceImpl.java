package ru.practicum.api.admin.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.WebClientService;
import ru.practicum.dto.ViewStatsDtoResponse;
import ru.practicum.dto.comment.CommentDtoShort;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.utility.EventState;
import ru.practicum.utility.RequestStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventsServiceImpl implements AdminEventsService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private final WebClientService baseClient;

    @Override
    @Transactional
    public List<EventFullDto> getEventsAdmin(List<Long> users,
                                             List<EventState> states,
                                             List<Long> categories,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             Pageable pageable,
                                             HttpServletRequest httpServletRequest) {

        List<Event> events = eventRepository.findAllByParams(users,
                states,
                categories,
                pageable);

        List<EventFullDto> eventFullDtoList;

        if (Optional.ofNullable(rangeStart).isPresent() && Optional.ofNullable(rangeEnd).isPresent()) {
            eventFullDtoList = events.stream()
                    .filter(o -> o.getEventDate().isAfter(rangeStart.minusNanos(1)))
                    .filter(o -> o.getEventDate().isBefore(rangeEnd.plusNanos(1)))
                    .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        } else if (Optional.ofNullable(rangeStart).isPresent()) {
            eventFullDtoList = events.stream()
                    .filter(o -> o.getEventDate().isAfter(rangeStart.minusNanos(1)))
                    .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        } else if (Optional.ofNullable(rangeEnd).isPresent()) {
            eventFullDtoList = events.stream()
                    .filter(o -> o.getEventDate().isBefore(rangeEnd.plusNanos(1)))
                    .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        } else {
            eventFullDtoList = events.stream()
                    .map(EventMapper::toEventFullDto).collect(Collectors.toList());
        }
        for (EventFullDto efd : eventFullDtoList) {
            efd.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(efd.getId(), RequestStatus.CONFIRMED));
            efd.setViews(setViewsToEventFullDtoList(efd, httpServletRequest));
            efd.setComments(setCommentsToEventFullDto(efd));
        }
        return eventFullDtoList;
    }

    @Override
    @Transactional
    public EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest httpServletRequest) {
        Event event = EventMapper.updateEventAdminRequest(eventRepository.findById(eventId)
                .orElseThrow(()
                        -> new NotFoundException("Событие не найдено!")), updateEventAdminRequest);

        if (updateEventAdminRequest.getCategory() > 0)
            event.setCategory(categoryRepository.findCategoryById(updateEventAdminRequest.getCategory()));

        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        eventFullDto.setComments(setCommentsToEventFullDto(eventFullDto));
        eventFullDto.setViews(setViewsToEventFullDto(httpServletRequest));
        return eventFullDto;
    }

    private List<CommentDtoShort> setCommentsToEventFullDto(EventFullDto eventFullDto) {
        return commentRepository.findAllByEventId(eventFullDto.getId()).stream()
                .map(CommentMapper::toCommentDtoShort).collect(Collectors.toList());
    }

    private long setViewsToEventFullDto(HttpServletRequest httpServletRequest) {
        String path = (httpServletRequest.getRequestURI());
        int charIndex = path.indexOf("/events");
        long views = 0;

        List<ViewStatsDtoResponse> list = baseClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(path.substring(charIndex)), true);

        for (ViewStatsDtoResponse v : list) {
            views = v.getHits();
        }

        return views;
    }

    private long setViewsToEventFullDtoList(EventFullDto eventFullDto, HttpServletRequest httpServletRequest) {

        String path = (httpServletRequest.getRequestURI() + "/" + eventFullDto.getId());
        int charIndex = path.indexOf("/events");
        long views = 0;

        List<ViewStatsDtoResponse> list = baseClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(path.substring(charIndex)), true);

        for (ViewStatsDtoResponse v : list) {
            views = v.getHits();
        }

        return views;
    }

}
