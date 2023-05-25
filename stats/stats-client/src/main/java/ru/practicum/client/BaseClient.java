package ru.practicum.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class BaseClient {
    protected final RestTemplate rest;
    private static final String GET_STATS_PREFIX = "/stats";
    private static final String POST_HIT_PREFIX = "/hit";


    public BaseClient(@Value("${server-address.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    protected ResponseEntity<Object> getViewStats(String start, String end, boolean isUnique) {
        String path = (GET_STATS_PREFIX + "?start=" + start + "&end=" + end + "&unique=" + isUnique);
        return makeAndSendRequest(HttpMethod.GET, path, null);
    }

    protected ResponseEntity<Object> getViewStats(String start, String end, List<String> uris, boolean isUnique) {
        StringBuilder urisString = new StringBuilder();
        for (String s : uris) {
            urisString.append(s);
            urisString.append(",");
        }
        String path = (GET_STATS_PREFIX + "?start=" + start + "&end=" + end + "&uris=" + urisString + "&unique=" + isUnique);
        return makeAndSendRequest(HttpMethod.GET, path, null);
    }

    protected <T> ResponseEntity<Object> postEndpointHit(T body) {
        String path = (POST_HIT_PREFIX);
        return makeAndSendRequest(HttpMethod.POST, path, body);
    }


    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> ewmStatServerResponse;
        try {
            ewmStatServerResponse = rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(ewmStatServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}


