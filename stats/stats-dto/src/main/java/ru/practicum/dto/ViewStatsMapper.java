package ru.practicum.dto;

import ru.practicum.model.ViewStats;

public class ViewStatsMapper {

    public static ViewStatsDtoResponse toDto(ViewStats viewStats) {
        return ViewStatsDtoResponse.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

    public static ViewStats fromDto(ViewStatsDtoResponse viewStatsDtoResponse) {
        return ViewStats.builder()
                .app(viewStatsDtoResponse.getApp())
                .uri(viewStatsDtoResponse.getUri())
                .hits(viewStatsDtoResponse.getHits())
                .build();
    }

}
