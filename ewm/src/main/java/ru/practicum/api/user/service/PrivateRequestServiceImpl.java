package ru.practicum.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.utility.EventState;
import ru.practicum.utility.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequestDto postRequest(long userId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие не найдено!"));

        if (event.getParticipantLimit() > 0 && requestRepository.countAllByEventIdAndStatus(eventId,
                RequestStatus.CONFIRMED) >= event.getParticipantLimit())
            throw new ForbiddenException("Невозможно подтвердить новую заявку – достигнут лимит заявок");

        if (Optional.ofNullable(requestRepository.findRequestByRequesterIdAndEventId(userId, eventId)).isPresent()) {
            throw new ForbiddenException("Нельзя подать заявку повторно");
        }

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new ForbiddenException("Невозможно подать заявку на участие в неопубликованном событии");

        if (event.getInitiator().getId() == userId)
            throw new ForbiddenException("Невозможно подать заявку на участие в собственном событии");

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(userRepository.findById(userId).orElseThrow(()
                        -> new NotFoundException("Пользователь не найден!")))
                .status(validateRequestStatus(event))
                .event(event)
                .build();
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    private RequestStatus validateRequestStatus(Event event) {
        if (event.getParticipantLimit() > 0 && event.isRequestModeration())
            return RequestStatus.PENDING;

        return RequestStatus.CONFIRMED;
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId) {
        return requestRepository.findAllByRequesterId(userId)
                .stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto patchRequestStateToCancel(long userId, long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

}
