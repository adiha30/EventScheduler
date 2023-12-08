package com.adiha.EventScheduler.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Event entity class.
 * This class represents an event in the system.
 */
@Entity
@Table(name = "events")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    /**
     * The unique identifier of the event.
     */
    @Id
    @UuidGenerator
    private String eventId;

    /**
     * The name of the event.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The creation time of the event.
     * By default, it is set to the current time.
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime creationTime = LocalDateTime.now();

    /**
     * The start time of the event.
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * The end time of the event.
     */
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * The user id of the user who created the event.
     */
    @Column(nullable = false)
    private String creatingUserId;

    /**
     * The location of the event.
     */
    private String location;

    /**
     * The venue of the event.
     */
    private String venue;

    /**
     * The set of user ids of the users who are subscribed to the event.
     */
    @ElementCollection
    @CollectionTable(
            name = "event_subscriptions",
            joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "user_id")
    private Set<String> users;

}