package com.adiha.EventScheduler.models.websocket;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.models.enums.UpdateType;
import lombok.*;

/**
 * This class represents an update related to an event.
 * It contains an Event object and an UpdateType enum.
 * The Event object represents the event that the update is about.
 * The UpdateType enum represents the type of the update (e.g. UPDATED, DELETED).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventUpdate {
    private Event event;
    private UpdateType updateType;
}