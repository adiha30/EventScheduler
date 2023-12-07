package com.adiha.EventScheduler.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime creationTime = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private UUID creatingUserId;

    private String location;

    private String venue;

    @ElementCollection
    @CollectionTable(
            name = "event_subscriptions",
            joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "user_id")
    private Set<UUID> users;

}
