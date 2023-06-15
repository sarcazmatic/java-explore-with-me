package ru.practicum.api.all.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.codec.DecodingException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.WebClientService;
import ru.practicum.dto.EndpointHitDtoRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.EventRepository;
import ru.practicum.utility.EWMDateTimePattern;
import ru.practicum.utility.EventState;
import org.apache.commons.lang3.BooleanUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {

    private final WebClientService baseClient;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EventFullDto getEventById(HttpServletRequest httpServletRequest, long id) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Событие не найдено")));

        if (eventFullDto.getState() == EventState.PUBLISHED) {
            EndpointHitDtoRequest endpointHitDtoRequest = EndpointHitDtoRequest.builder()
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                    .uri(httpServletRequest.getRequestURI())
                    .app("ewm-main-service")
                    .ip(httpServletRequest.getRemoteAddr()).build();
            try {
                baseClient.postHit(endpointHitDtoRequest.getApp(),
                        endpointHitDtoRequest.getUri(),
                        endpointHitDtoRequest.getIp(),
                        endpointHitDtoRequest.getTimestamp());
            } catch (DecodingException ignored) {
            }

            return setViewsToEventFullDto(eventFullDto, httpServletRequest);
        }

        throw new NotFoundException("Нет подходящих опубликованных событий");
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsFiltered(String text,
                                                List<Long> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Boolean onlyAvailable,
                                                String sort,
                                                Pageable pageable,
                                                HttpServletRequest httpServletRequest) {

        List<EventFullDto> eventFullDtos = (BooleanUtils.isTrue(onlyAvailable)) ?
                eventRepository.findAllByParamsPublic(text,
                                categories,
                                paid,
                                rangeStart,
                                rangeEnd,
                                sort,
                                pageable)
                        .stream()
                        .map(EventMapper::toEventFullDto)
                        .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit())
                        .collect(Collectors.toList())
                :
                eventRepository.findAllByParamsPublic(text,
                                categories,
                                paid,
                                rangeStart,
                                rangeEnd,
                                sort,
                                pageable)
                        .stream()
                        .map(EventMapper::toEventFullDto)
                        .collect(Collectors.toList());


        for (EventFullDto e : eventFullDtos) {
            EndpointHitDtoRequest endpointHitDtoRequest = EndpointHitDtoRequest.builder()
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER)))
                    .uri(httpServletRequest.getRequestURI() + "/" + e.getId())
                    .app("ewm-main-service")
                    .ip(httpServletRequest.getRemoteAddr()).build();
            try {
                baseClient.postHit(endpointHitDtoRequest.getApp(),
                        endpointHitDtoRequest.getUri(),
                        endpointHitDtoRequest.getIp(),
                        endpointHitDtoRequest.getTimestamp());
            } catch (DecodingException ignored) {
            }
            setViewsToEventFullDtoList(e, httpServletRequest);
        }

        return eventFullDtos;
    }

    private EventFullDto setViewsToEventFullDto(EventFullDto eventFullDto, HttpServletRequest httpServletRequest) {

        eventFullDto.setViews(baseClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(httpServletRequest.getRequestURI()),
                true).get(0).getHits());

        return eventFullDto;
    }

    private void setViewsToEventFullDtoList(EventFullDto eventFullDto, HttpServletRequest httpServletRequest) {

        try {
            eventFullDto.setViews(baseClient.getStats(LocalDateTime.now().minusYears(100),
                    LocalDateTime.now().plusYears(100),
                    List.of(httpServletRequest.getRequestURI() + "/" + eventFullDto.getId()),
                    true).get(0).getHits());
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

}
