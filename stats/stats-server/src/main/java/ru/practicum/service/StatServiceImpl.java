package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDtoRequest;
import ru.practicum.dto.EndpointHitMapper;
import ru.practicum.dto.ViewStatsDtoResponse;
import ru.practicum.dto.ViewStatsMapper;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseEntity<Object> postEndpointHit(EndpointHitDtoRequest endpointHitDtoRequest) {
        statRepository.save(EndpointHitMapper.fromDto(endpointHitDtoRequest));
        log.info("Создали хит {} в базе данных", endpointHitDtoRequest);
        return new ResponseEntity("Информация сохранена", HttpStatus.CREATED);
    }

    @Override
    public List<ViewStatsDtoResponse> getViewStats(String start, String end, List<String> uris, boolean isUnique) {
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);

        List<ViewStats> list;
        if (uris.isEmpty()) {
            if (!isUnique) {
                list = statRepository.findAllByCreatedBetweenWithoutUniqueIp(startTime, endTime);
                log.info("Возвращаем список обращений размером без фильтра по uri и уникальным ip {}", list.size());
                return list.stream().map(o -> ViewStatsMapper.toDto(o)).collect(Collectors.toList());
            } else {
                list = statRepository.findByCreatedBetweenWithUniqueIp(startTime, endTime);
                log.info("Возвращаем список обращений размером без фильтра по uri, с фильтром по уникальным ip {}", list.size());
                return list.stream().map(o -> ViewStatsMapper.toDto(o)).collect(Collectors.toList());
            }
        } else {
            if (!isUnique) {
                list = statRepository.findAllByCreatedBetweenWithoutUniqueIpIsInUris(startTime, endTime, uris);
                log.info("Возвращаем список обращений размером с фильтром по uri и без фильтра по уникальным ip {}", list.size());
                return list.stream().map(o -> ViewStatsMapper.toDto(o)).collect(Collectors.toList());
            } else {
                list = statRepository.findByCreatedBetweenWithUniqueIpIsInUris(startTime, endTime, uris);
                log.info("Возвращаем список обращений размером с фильтром по uri и уникальным ip {}", list.size());
                return list.stream().map(o -> ViewStatsMapper.toDto(o)).collect(Collectors.toList());
            }
        }
    }
}
