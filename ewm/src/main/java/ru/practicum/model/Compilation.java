package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compilations")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private boolean pinned;
    @Column(length = 50, nullable = false)
    private String title;
    @ManyToMany
    @JoinTable(name = "compilations_events", joinColumns = {@JoinColumn(
            name = "compilation_fk")},
            inverseJoinColumns = {@JoinColumn(name = "event_fk")})
    private List<Event> events = new ArrayList<>();
}
