package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDtoRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class BaseController {

    private final BaseClient baseClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> getViewStats(HttpServletRequest request,
                                               @RequestParam @NotNull String start,
                                               @RequestParam @NotNull String end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(name = "unique", required = false, defaultValue = "false") boolean isUnique) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        if (request.getParameter("uris") == null) {
            return baseClient.getViewStats(start, end, isUnique);
        } else {
            return baseClient.getViewStats(start, end, uris, isUnique);
        }
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> postEndpointHit(HttpServletRequest request,
                                                  @RequestBody @Valid EndpointHitDtoRequest endpointHitDtoRequest) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return baseClient.postEndpointHit(endpointHitDtoRequest);
    }

}
