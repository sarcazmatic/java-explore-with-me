package ru.practicum.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ViewStats {

    private String app;
    private String uri;
    private long hits;

}
