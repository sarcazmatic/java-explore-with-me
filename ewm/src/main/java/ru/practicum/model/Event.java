package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.utility.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 2000, nullable = false)
    private String annotation;
    @ManyToOne
    @CollectionTable(name = "events_category", joinColumns = @JoinColumn(name = "category_id", nullable = false))
    private Category category;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(length = 7000, nullable = false)
    private String description;
    @Column(name = "event_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @CollectionTable(name = "events_initiator", joinColumns = @JoinColumn(name = "initiator_id", nullable = false))
    private User initiator;
    @Column(name = "location_lat", nullable = false)
    private float lat;
    @Column(name = "location_lon", nullable = false)
    private float lon;
    @Column(nullable = false)
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private EventState state;
    @Column(length = 120)
    private String title;

}
