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
@Table(name = "events", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String annotation;
    @ManyToOne
    @CollectionTable(name = "events_category", joinColumns = @JoinColumn(name = "category_id"))
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime eventDate;
    @ManyToOne
    @CollectionTable(name = "events_initiator", joinColumns = @JoinColumn(name = "initiator_id"))
    private User initiator;
    @Column(name = "location_lat")
    private float lat;
    @Column(name = "location_lon")
    private float lon;
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;

}
