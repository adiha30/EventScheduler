package com.adiha.EventScheduler;

import com.adiha.EventScheduler.models.Event;

import java.time.LocalDateTime;
import java.util.Set;

public class TestUtils {
    public static Event getSimpleEvent(String id) {
        return Event.builder()
                .eventId(id)
                .name("Test Event")
                .creatingUserId("1")
                .location("Test Location")
                .venue("Test Venue")
                .creationTime(LocalDateTime.now())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .users(Set.of("1"))
                .build();
    }
}
