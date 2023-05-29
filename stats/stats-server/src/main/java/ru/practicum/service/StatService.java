package ru.practicum.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.EndpointHitDtoRequest;
import ru.practicum.dto.ViewStatsDtoResponse;

import java.util.List;

public interface StatService {

    ResponseEntity<Object> postEndpointHit(EndpointHitDtoRequest endpointHitDtoRequest);

    List<ViewStatsDtoResponse> getViewStats(String start, String end, List<String> uris, boolean isUnique);

}
