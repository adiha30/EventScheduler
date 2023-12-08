package com.adiha.EventScheduler.services.crud;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.services.EventsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.adiha.EventScheduler.TestUtils.getSimpleEvent;
import static com.google.common.truth.Truth.assertWithMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UpdateOperationsTest {

    @Autowired
    private EventsService sut;

    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("Test update valid event")
    @Transactional
    void testUpdateValidEvent() {
        Event event1 = getSimpleEvent();
        eventRepository.save(event1);

        sut.updateEvent(event1.getEventId(), event1.toBuilder().name("New Name").build());
        Event updatedEvent = eventRepository.findById(event1.getEventId()).get();

        Assertions.assertAll(
                () -> assertWithMessage("Updated event is null")
                        .that(updatedEvent)
                        .isNotNull(),
                () -> assertWithMessage("Event was not updated")
                        .that(eventRepository.findById(updatedEvent.getEventId()).get())
                        .isEqualTo(updatedEvent)
        );
    }

    @Test
    @DisplayName("Test update event with null event")
    @Transactional
    void testUpdateEventWithNullEvent() {
        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> sut.updateEvent("1", null)
        );
    }
}
