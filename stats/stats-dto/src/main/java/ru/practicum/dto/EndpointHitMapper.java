package ru.practicum.dto;

import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHitDtoRequest toDto(EndpointHit endpointHit) {
        return EndpointHitDtoRequest.builder()
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(endpointHit.getCreated().format(FORMATTER))
                .build();
    }

    public static EndpointHit fromDto(EndpointHitDtoRequest endpointHitDtoRequest) {
        return EndpointHit.builder()
                .app(endpointHitDtoRequest.getApp())
                .ip(endpointHitDtoRequest.getIp())
                .uri(endpointHitDtoRequest.getUri())
                .created(LocalDateTime.parse(endpointHitDtoRequest.getTimestamp(), FORMATTER))
                .build();
    }

}
