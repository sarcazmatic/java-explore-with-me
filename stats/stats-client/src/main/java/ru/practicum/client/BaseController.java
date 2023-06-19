package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDtoRequest;
import ru.practicum.dto.ViewStatsDtoResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class BaseController {

    private final WebClientService baseClient;
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    @GetMapping("/stats")
    public List<ViewStatsDtoResponse> getViewStats(HttpServletRequest request,
                                                   @RequestParam @NotNull String start,
                                                   @RequestParam @NotNull String end,
                                                   @RequestParam(required = false) List<String> uris,
                                                   @RequestParam(name = "unique", required = false, defaultValue = "false") boolean isUnique) {
        log.info("client ip: {}, endpoint path: {}", request.getRemoteAddr(), request.getRequestURI());
        List<String> emptyUris = new ArrayList<>();
        return (request.getParameter("uris") == null
                ? baseClient.getStats(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(TIME_PATTERN)),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern(TIME_PATTERN)),
                emptyUris,
                isUnique)
                : baseClient.getStats(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(TIME_PATTERN)),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern(TIME_PATTERN)),
                uris,
                isUnique));
    }

    @PostMapping("/hit")
    public void postEndpointHit(HttpServletRequest request,
                                @RequestBody @Valid EndpointHitDtoRequest endpointHitDtoRequest) {
        log.info("client ip: {}, endpoint path: {}", request.getRemoteAddr(), request.getRequestURI());
        baseClient.postHit(endpointHitDtoRequest.getApp(),
                endpointHitDtoRequest.getUri(),
                endpointHitDtoRequest.getIp(),
                endpointHitDtoRequest.getTimestamp());
    }

}
