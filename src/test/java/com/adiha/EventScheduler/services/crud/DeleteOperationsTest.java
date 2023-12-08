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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.adiha.EventScheduler.TestUtils.getSimpleEvent;
import static com.google.common.truth.Truth.assertWithMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class DeleteOperationsTest {

    @Autowired
    private EventsService sut;

    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("Test delete valid event")
    @Transactional
    void testDeleteValidEvent() {
        Event event1 = getSimpleEvent();
        eventRepository.save(event1);

        sut.deleteEvent(event1.getEventId());

        Truth.assertThat(eventRepository.findById(event1.getEventId()).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Test delete event with invalid event id")
    @Transactional
    void testDeleteEventWithInvalidEventId() {
        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> sut.deleteEvent("1")
        );
    }

    @Test
    @DisplayName("Test delete event with null event id")
    @Transactional
    void testDeleteEventWithNullEventId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> sut.deleteEvent(null)
        );
    }

    @Test
    @DisplayName("Test delete valid events")
    @Transactional
    void testDeleteValidEvents() {
        Event event1 = getSimpleEvent();
        Event event2 = getSimpleEvent();
        eventRepository.save(event1);
        eventRepository.save(event2);

        sut.deleteAll(List.of(event1.getEventId(), event2.getEventId()));

        Assertions.assertAll(
                () -> assertWithMessage("Event with id " + event1.getEventId() + " was not deleted")
                        .that(eventRepository.findById(event1.getEventId()).isPresent())
                        .isFalse(),
                () -> assertWithMessage("Event with id " + event2.getEventId() + " was not deleted")
                        .that(eventRepository.findById(event2.getEventId()).isPresent())
                        .isFalse()
        );
    }

}
