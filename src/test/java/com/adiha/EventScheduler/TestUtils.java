package com.adiha.EventScheduler;

import com.adiha.EventScheduler.models.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TestUtils {
    public static final String VENUE1 = "Arena";
    public static final String VENUE2 = "Stadium";
    public static final String LOCATION1 = "California";
    public static final String LOCATION2 = "Tel Aviv";

    public static Event getSimpleEvent() {
        return Event.builder()
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

    public static Event getSimpleEvent(Set<String> users) {
        return Event.builder()
                .name("Test Event")
                .creatingUserId("1")
                .location("Test Location")
                .venue("Test Venue")
                .creationTime(LocalDateTime.now())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .users(users)
                .build();
    }

    public static Event getSimpleEvent(LocalDateTime startTime) {
        return Event.builder()
                .name("Test Event")
                .creatingUserId("1")
                .location("Test Location")
                .venue("Test Venue")
                .creationTime(LocalDateTime.now())
                .startTime(startTime)
                .endTime(LocalDateTime.now().plusHours(1))
                .users(Set.of("1"))
                .build();
    }

    public static Event getSimpleEvent(String venue, String locaion) {
        return Event.builder()
                .name("Test Event")
                .creatingUserId("1")
                .location(locaion)
                .venue(venue)
                .creationTime(LocalDateTime.now())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .users(Set.of("1"))
                .build();
    }
}
