package ru.practicum.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class EndpointHit {

    private int id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime created;

}
