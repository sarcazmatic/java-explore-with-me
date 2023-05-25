package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ViewStatsDtoResponse {

    private String app;
    private String uri;
    private Long hits;

}
