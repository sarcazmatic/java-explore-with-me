package ru.practicum.api.all.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.WebClientService;
import ru.practicum.dto.EndpointHitDtoRequest;
import ru.practicum.dto.comment.CommentDtoResponse;
import ru.practicum.dto.comment.CommentDtoShort;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.utility.CommentStatus;
import ru.practicum.utility.EWMDateTimePattern;
import ru.practicum.utility.EventState;
import org.apache.commons.lang3.BooleanUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicEventsServiceImpl implements PublicEventsService {

    private final WebClientService baseClient;
    private final EventRepository eventRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

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
                log.warn("Произошла ошибка кодировки при обращении к клиенту статистики!");
            }
            eventFullDto.setComments(setCommentsToEventFullDto(eventFullDto));
            eventFullDto.setViews(setViewsToEventFullDto(httpServletRequest));
            return eventFullDto;
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
                        .filter(e -> e.getState() == EventState.PUBLISHED)
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
                        .filter(e -> e.getState() == EventState.PUBLISHED)
                        .collect(Collectors.toList());


        for (EventFullDto e : eventFullDtos) {
            if (e.getState() == EventState.PUBLISHED) {
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
                    log.warn("Произошла ошибка кодировки при обращении к клиенту статистики!");
                }
            }
            try {
                e.setViews(setViewsToEventFullDtoList(e, httpServletRequest));
            } catch (IndexOutOfBoundsException ignored) {
                log.warn("Произошла ошибка при обращении к клиенту статистики!");
            }
            e.setComments(setCommentsToEventFullDto(e));
        }

        return eventFullDtos;
    }

    private long setViewsToEventFullDto(HttpServletRequest httpServletRequest) {

        return baseClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(httpServletRequest.getRequestURI()),
                true).get(0).getHits();

    }

    private long setViewsToEventFullDtoList(EventFullDto eventFullDto, HttpServletRequest httpServletRequest) throws IndexOutOfBoundsException {

        return baseClient.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(httpServletRequest.getRequestURI() + "/" + eventFullDto.getId()),
                true).get(0).getHits();

    }

    @Override
    public List<CommentDtoResponse> getComments(long eventId) {

        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        List<CommentDtoResponse> commentDtoResponseList = new ArrayList<>();

        for (Comment c : comments) {
            CommentDtoResponse commentDtoResponse = CommentMapper.toCommentDtoResponse(c);
            commentDtoResponse.setCommenter(UserMapper.toUserShortDto(userRepository.findById(c.getCommenter().getId()).orElseThrow(
                    () -> new NotFoundException("Пользователь не найден!")
            )));
            commentDtoResponse.setEvent(EventMapper.toEventShortDto(eventRepository.findById(eventId).orElseThrow(
                    () -> new NotFoundException("Событие не найдено!")
            )));
            commentDtoResponseList.add(commentDtoResponse);
        }
        return commentDtoResponseList;
    }

    private List<CommentDtoShort> setCommentsToEventFullDto(EventFullDto eventFullDto) {
        return commentRepository.findAllByEventIdAndCommentStatus(eventFullDto.getId(), CommentStatus.CONFIRMED).stream()
                .map(CommentMapper::toCommentDtoShort).collect(Collectors.toList());
    }

}
