package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;
import ru.practicum.utility.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(long userId);

    List<Request> findAllByEventIdAndIdIn(long eventId, List<Long> requestIds);

    List<Request> findAllByEventId(long evenId);

    Request findRequestByRequesterIdAndEventId(long requesterId, long eventId);

    Integer countAllByEventIdAndStatus(long eventId, RequestStatus requestStatus);

    Integer countAllByEventId(long eventId);


}
