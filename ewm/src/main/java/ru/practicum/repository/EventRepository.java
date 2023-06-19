package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Event;
import ru.practicum.utility.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(long userId, long eventId);

    @Query("SELECT e FROM Event e WHERE ((:users IS NOT NULL AND e.initiator.id IN :users) OR (:users IS NULL)) " +
            "AND ((:states IS NOT NULL AND e.state IN :states) OR (:states IS NULL)) " +
            "AND ((:categories IS NOT NULL AND e.category.id IN :categories) OR (:categories IS NULL))")
    List<Event> findAllByParams(@Param("users") List<Long> users,
                                @Param("states") List<EventState> states,
                                @Param("categories") List<Long> categories,
                                Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (:text IS NOT NULL AND UPPER(e.annotation) LIKE CONCAT('%', UPPER(:text), '%') " +
            "OR (:text IS NOT NULL AND UPPER(e.description) LIKE CONCAT('%', UPPER(:text), '%')) " +
            "OR (:text IS NULL)) " +
            "AND ((:categories IS NOT NULL AND e.category.id IN :categories) OR (:categories IS NULL)) " +
            "AND ((:paid IS TRUE AND e.paid = TRUE) OR (:paid IS FALSE AND e.paid = FALSE) OR (:paid IS NULL)) " +
            "AND (e.eventDate >= :rangeStart) " +
            "AND (e.eventDate <= :rangeEnd) " +
            "ORDER BY :sort ASC")
    List<Event> findAllByParamsPublic(@Param("text") String text,
                                      @Param("categories") List<Long> categories,
                                      @Param("paid") Boolean paid,
                                      @Param("rangeStart") LocalDateTime rangeStart,
                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                      @Param("sort") String sort,
                                      Pageable pageable);

}
