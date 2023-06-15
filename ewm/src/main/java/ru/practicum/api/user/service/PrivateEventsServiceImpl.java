package ru.practicum.api.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.utility.RequestStatus;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventsServiceImpl implements PrivateEventsService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventFullDto postEvent(long userId, NewEventDto newEventDto) {
        log.info("Создается Event {} от User {}", newEventDto, userId);
        Event newEvent = EventMapper.fromNewEventDto(newEventDto);
        newEvent.setInitiator(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден!")
        ));
        newEvent.setCategory(categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Категория не найдена!")
        ));
        return EventMapper.toEventFullDto(eventRepository.save(newEvent));
    }

    @Override
    @Transactional
    public List<EventShortDto> getEvents(long userId, Pageable pageable) {
        log.info("Выводим список Event от User {}", userId);
        return eventRepository.findAllByInitiatorId(userId, pageable)
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto getEventById(long userId, long eventId) {
        log.info("Выводим Event {} от User {}", eventId, userId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.findByInitiatorIdAndId(userId, eventId));
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult patchRequests(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        EventFullDto eventFullDto = getEventById(userId, eventId);
        List<Request> requestStatusUpdateList = requestRepository.findAllByEventIdAndIdIn(eventId, eventRequestStatusUpdateRequest.getRequestIds());
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        if (eventFullDto.getParticipantLimit() > 0) {
            for (Request r : requestStatusUpdateList) {
                if (eventFullDto.getConfirmedRequests() >= eventFullDto.getParticipantLimit())
                    throw new ForbiddenException("Невозможно подтвердить новую заявку – достигнут лимит");

                Request request = requestRepository.findById(r.getId()).orElseThrow(
                        () -> new NotFoundException("Запрос не найден")
                );

                if (r.getStatus() != RequestStatus.PENDING)
                    throw new ForbiddenException("Сменить статус можно только у заявок в статусе PENDING!");

                request.setStatus(eventRequestStatusUpdateRequest.getStatus());
                requestRepository.save(request);

                if (request.getStatus() == RequestStatus.CONFIRMED) {
                    confirmedRequests.add(RequestMapper.toParticipationRequestDto(request));
                    eventFullDto.setConfirmedRequests(eventFullDto.getConfirmedRequests() + 1);
                }

                if (request.getStatus() == RequestStatus.REJECTED)
                    rejectedRequests.add(RequestMapper.toParticipationRequestDto(request));
            }
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = EventMapper.updateEventUserRequest(eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие не найдено")
        ), updateEventUserRequest);

        if (event.getInitiator().getId() != userId)
            throw new ForbiddenException("Нельзя менять чужие события!");

        if (updateEventUserRequest.getCategory() > 0)
            event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена")));

        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        return eventFullDto;
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> getRequestsForEvent(long userId, long eventId) {
        if (!eventRepository.findAllByInitiatorId(userId, Pageable.unpaged()).contains(eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("У данного пользователя не инициировано событий"))))
            throw new NotFoundException("Данное событие инициировано другим пользователем");
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

}
