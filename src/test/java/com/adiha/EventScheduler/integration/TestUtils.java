package com.adiha.EventScheduler.integration;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.models.User;

import java.time.LocalDateTime;

import static com.adiha.EventScheduler.models.enums.Role.USER;

public class TestUtils {

    public static final String TEST_USER = "testUser";
    public static final String TEST_PASSWORD = "testPassword";
    public static final String TEST_UUID = "12345678-1234-1234-1234-123456789012";

    public static Event createSampleEvent(String eventName, String creatingUserId) {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);

        return Event.builder()
                .name(eventName)
                .startTime(startTime)
                .endTime(endTime)
                .creatingUserId(creatingUserId)
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
