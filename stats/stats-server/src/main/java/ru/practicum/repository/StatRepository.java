package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(h.id)) FROM EndpointHit h " +
            "WHERE h.created > :start AND h.created < :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<ViewStats> findAllByCreatedBetween(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) FROM EndpointHit h " +
            "WHERE h.created > :start AND h.created < :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<ViewStats> findAllByCreatedBetweenWithUniqueIp(@Param("start") LocalDateTime start,
                                                        @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(h.id)) FROM EndpointHit h " +
            "WHERE h.created > :start AND h.created < :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<ViewStats> findAllByCreatedBetweenWithoutUniqueIpIsInUris(LocalDateTime start,
                                                                   LocalDateTime end,
                                                                   @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) FROM EndpointHit h " +
            "WHERE h.created > :start AND h.created < :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<ViewStats> findByCreatedBetweenWithUniqueIpIsInUris(@Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end,
                                                             @Param("uris") List<String> uris);

}
