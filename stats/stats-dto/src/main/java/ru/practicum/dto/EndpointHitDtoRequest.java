package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;


@Builder
@Getter
public class EndpointHitDtoRequest {

    @NotNull
    private String app;
    @NotNull
    private String uri;
    @NotNull
    private String ip;
    @NotNull
    private String timestamp;

}
