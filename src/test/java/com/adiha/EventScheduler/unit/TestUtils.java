package com.adiha.EventScheduler.unit;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.models.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.adiha.EventScheduler.models.enums.Role.USER;

public class TestUtils {
    public static final String VENUE1 = "Arena";
    public static final String VENUE2 = "Stadium";
    public static final String LOCATION1 = "California";
    public static final String LOCATION2 = "Tel Aviv";
    public static final String TEST_USER = "testUser";
    public static final String TEST_PASSWORD = "testPassword";
    public static final String TEST_EVENT_NAME = "Test Event";
    public static final String TEST_UUID = "12345678-1234-1234-1234-123456789012";


    public static Event getSimpleEvent() {
        HashSet<String> subscribers = new HashSet<>();
        subscribers.add("1");

        return Event.builder()
                .name(TEST_EVENT_NAME)
                .creatingUserId(TEST_UUID)
                .location("Test Location")
                .venue("Test Venue")
                .creationTime(LocalDateTime.now())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .subscribers(subscribers)
                .build();
    }

    public static Event getSimpleEvent(Set<String> subscribers) {
        return Event.builder()
                .name(TEST_EVENT_NAME)
                .creatingUserId(TEST_UUID)
                .location("Test Location")
                .venue("Test Venue")
                .creationTime(LocalDateTime.now())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .subscribers(subscribers)
                .build();
    }

    public static Event getSimpleEvent(LocalDateTime startTime) {
        HashSet<String> subscribers = new HashSet<>();
        subscribers.add("1");

        return Event.builder()
                .name(TEST_EVENT_NAME)
                .creatingUserId(TEST_UUID)
                .location("Test Location")
                .venue("Test Venue")
                .creationTime(LocalDateTime.now())
                .startTime(startTime)
                .endTime(LocalDateTime.now().plusHours(1))
                .subscribers(subscribers)
                .build();
    }

    public static Event getSimpleEvent(String venue, String locaion) {
        HashSet<String> subscribers = new HashSet<>();
        subscribers.add("1");

        return Event.builder()
                .name(TEST_EVENT_NAME)
                .creatingUserId(TEST_UUID)
                .location(locaion)
                .venue(venue)
                .creationTime(LocalDateTime.now())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .subscribers(subscribers)
                .build();
    }

    public static User getSimpleUser() {
        return User.builder()
                .userId(TEST_UUID)
                .username(TEST_USER)
                .password(TEST_PASSWORD)
                .role(USER)
                .build();
    }
}
