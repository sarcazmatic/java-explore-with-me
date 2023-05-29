package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDtoRequest;
import ru.practicum.dto.ViewStatsDtoResponse;
import ru.practicum.service.StatService;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class StatController {

    private final StatService statService;

    @GetMapping("/stats")
    public List<ViewStatsDtoResponse> getViewStats(HttpServletRequest request,
                                             @RequestParam String start,
                                             @RequestParam String end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(name = "unique", required = false, defaultValue = "false") boolean isUnique) {
        log.info("client ip: {}, endpoint path: {}", request.getRemoteAddr(), request.getRequestURI());
        return (request.getParameter("uris") == null
                ? statService.getViewStats(start, end, new ArrayList<>(), isUnique)
                : statService.getViewStats(start, end, uris, isUnique));
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> postEndpointHit(HttpServletRequest request,
                                                  @RequestBody EndpointHitDtoRequest endpointHitDtoRequest) {
        log.info("client ip: {}, endpoint path: {}", request.getRemoteAddr(), request.getRequestURI());
        return statService.postEndpointHit(endpointHitDtoRequest);
    }

}
