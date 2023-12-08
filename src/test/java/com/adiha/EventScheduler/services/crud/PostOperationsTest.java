package com.adiha.EventScheduler.services.crud;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.services.EventsService;
import com.google.common.truth.Truth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.adiha.EventScheduler.TestUtils.getSimpleEvent;
import static com.google.common.truth.Truth.assertWithMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PostOperationsTest {

    @Autowired
    private EventsService sut;

    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("Test create event")
    @Transactional
    void testCreateEvent() {
        Event event1 = getSimpleEvent();

        Event createdEvent = sut.createEvent(event1);

        Assertions.assertAll(
                () -> assertWithMessage("Created event is null")
                        .that(createdEvent)
                        .isNotNull(),
                () -> assertWithMessage("Event was not inserted")
                        .that(eventRepository.findById(createdEvent.getEventId()).get())
                        .isEqualTo(event1)
        );
    }

    @Test
    @DisplayName("Test create event with null event")
    @Transactional
    void testCreateEventWithNullEvent() {
        Assertions.assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> sut.createEvent(null)
        );
    }

}
