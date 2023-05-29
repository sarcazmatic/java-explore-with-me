package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
@Getter
public class ViewStatsDtoResponse {

    private String app;
    private String uri;
    private Long hits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewStatsDtoResponse that = (ViewStatsDtoResponse) o;
        return Objects.equals(app, that.app) && Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri);
    }
}
