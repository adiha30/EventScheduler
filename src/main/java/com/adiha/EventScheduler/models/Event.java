package com.adiha.EventScheduler.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
     * The reminderSent field of the event.
     * This field represents whether a reminder has been sent for the event.
     */
    private boolean reminderSent;

    /**
     * The set of user ids of the users who are subscribed to the event.
     */
    @ElementCollection
    @CollectionTable(
            name = "event_subscriptions",
            joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "user_id")
    private Set<String> subscribers;

    /**
     * Adds a user to the subscribers of the event.
     *
     * @param userId The unique identifier of the user to be added to the subscribers.
     */
    public void addToSubscribers(String userId) {
        if (subscribers == null) {
            subscribers = new HashSet<>();
            subscribers.add(userId);
        } else {
            subscribers.add(userId);
        }
    }

    /**
     * This method is used to remove a user from the subscribers of the event.
     * If the subscribers set is not null and contains the user, it removes the user from the subscribers.
     * If the subscribers set is null or does not contain the user, it throws an IllegalArgumentException.
     *
     * @param userId The unique identifier of the user to be removed from the subscribers.
     * @throws IllegalArgumentException if the user is not subscribed to this event or the event does not exist.
     */
    public void removeFromSubscribers(String userId) {
        if (subscribers != null && subscribers.contains(userId)) {
            subscribers.remove(userId);
        } else {
            throw new IllegalArgumentException("User is not subscribed to this event or event does not exist.");
        }
    }

}