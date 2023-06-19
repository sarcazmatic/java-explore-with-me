package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.Event;
import ru.practicum.utility.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@AllArgsConstructor
public class EventMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER);

    public static Event fromNewEventDto(NewEventDto newEventDto) {
        Event event = Event.builder()
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .annotation(newEventDto.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .build();

        Optional.ofNullable(newEventDto.getRequestModeration()).ifPresentOrElse(
                (event::setRequestModeration),
                () -> event.setRequestModeration(true)
        );

        if (Optional.ofNullable(newEventDto.getEventDate()).isPresent()) {
            LocalDateTime newEventDate = LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER);

            if (!newEventDate.isAfter(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Дата события менее чем за два часа до редактирования");
            }

            event.setEventDate(newEventDate);
        }

        return event;

    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.isPaid())
                .publishedOn(getPublishedTime(event.getPublishedOn()))
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(0L)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(0)
                .build();
    }

    private static String getPublishedTime(LocalDateTime publishedOn) {
        try {
            return publishedOn.format(FORMATTER);
        } catch (NullPointerException e) {
            return "Событие еще не опубликовано";
        }
    }

    public static Event updateEventAdminRequest(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        Optional.ofNullable(updateEventAdminRequest.getAnnotation()).ifPresent(event::setAnnotation);

        Optional.ofNullable(updateEventAdminRequest.getDescription()).ifPresent(event::setDescription);

        if (Optional.ofNullable(updateEventAdminRequest.getEventDate()).isPresent()) {
            LocalDateTime newEventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), DateTimeFormatter.ofPattern(EWMDateTimePattern.FORMATTER));

            if (!newEventDate.isAfter(LocalDateTime.now().plusHours(1))) {
                throw new ValidationException("Дата события менее чем за час до редактирования");
            }
            event.setEventDate(newEventDate);
        }

        if (Optional.ofNullable(updateEventAdminRequest.getLocation()).isPresent()) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }

        Optional.ofNullable(updateEventAdminRequest.getPaid()).ifPresent(event::setPaid);

        if (updateEventAdminRequest.getParticipantLimit() > 0)
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());

        Optional.ofNullable(updateEventAdminRequest.getRequestModeration()).ifPresent(event::setRequestModeration);

        if (Optional.ofNullable(updateEventAdminRequest.getStateAction()).isPresent()) {
            if (updateEventAdminRequest.getStateAction().equals(EventAdminState.PUBLISH_EVENT) && event.getState().equals(EventState.PUBLISHED)) {
                throw new ForbiddenException("Нельзя опубликовать опубликованное событие!");
            } else if (updateEventAdminRequest.getStateAction().equals(EventAdminState.PUBLISH_EVENT) && event.getState().equals(EventState.CANCELED)) {
                throw new ForbiddenException("Нельзя опубликовать отмененное событие!");
            } else if (updateEventAdminRequest.getStateAction().equals(EventAdminState.REJECT_EVENT) && event.getState().equals(EventState.CANCELED)) {
                throw new ForbiddenException("Нельзя отменить отмененное событие!");
            } else if (updateEventAdminRequest.getStateAction().equals(EventAdminState.REJECT_EVENT) && event.getState().equals(EventState.PUBLISHED)) {
                throw new ForbiddenException("Нельзя отменить опубликованное событие!");
            } else if (updateEventAdminRequest.getStateAction().equals(EventAdminState.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else if (updateEventAdminRequest.getStateAction().equals(EventAdminState.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }

        Optional.ofNullable(updateEventAdminRequest.getTitle()).ifPresent(event::setTitle);

        return event;
    }

    public static Event updateEventUserRequest(Event event, UpdateEventUserRequest updateEventUserRequest) {

        if (!event.getState().equals(EventState.CANCELED) && !event.getState().equals(EventState.PENDING))
            throw new ForbiddenException("Нельзя имзменить опубликованные события!");

        Optional.ofNullable(updateEventUserRequest.getAnnotation()).ifPresent(event::setAnnotation);

        Optional.ofNullable(updateEventUserRequest.getDescription()).ifPresent(event::setDescription);

        if (Optional.ofNullable(updateEventUserRequest.getEventDate()).isPresent()) {
            LocalDateTime newEventDate = LocalDateTime.parse(updateEventUserRequest.getEventDate(), FORMATTER);

            if (!newEventDate.isAfter(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Дата события менее чем за два часа до редактирования");
            }
                event.setEventDate(newEventDate);
        }

        if (Optional.ofNullable(updateEventUserRequest.getLocation()).isPresent()) {
            event.setLat(updateEventUserRequest.getLocation().getLat());
            event.setLon(updateEventUserRequest.getLocation().getLon());
        }

        Optional.ofNullable(updateEventUserRequest.getPaid()).ifPresent(event::setPaid);

        if (updateEventUserRequest.getParticipantLimit() > 0)
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());

        Optional.ofNullable(updateEventUserRequest.getRequestModeration()).ifPresent(event::setRequestModeration);

        if (Optional.ofNullable(updateEventUserRequest.getStateAction()).isPresent()) {
            if (updateEventUserRequest.getStateAction().equals(EventUserState.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (updateEventUserRequest.getStateAction().equals(EventUserState.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }
        Optional.ofNullable(updateEventUserRequest.getTitle()).ifPresent(event::setTitle);

        return event;
    }

}
