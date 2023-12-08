package com.adiha.EventScheduler.services;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.adiha.EventScheduler.TestUtils.getSimpleEvent;
import static com.adiha.EventScheduler.utils.Constants.CREATION_TIME;
import static com.google.common.truth.Truth.assertWithMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EventsServiceTest {
    public static final String ASCENDING = "ASC";

    @Autowired
    private EventsService sut;

    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("Test get all events with default sorting")
    @Transactional
    void testGetAllEventsWithoutSorting() {
        Event event1 = getSimpleEvent("1");
        Event event2 = getSimpleEvent("2");
        List<Event> insertedEvents = List.of(event1, event2);
        eventRepository.saveAll(insertedEvents);

        List<Event> allEvents = sut.getAll(CREATION_TIME, ASCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not the same as all events' size")
                        .that(insertedEvents.size())
                        .isEqualTo(allEvents.size()),
                () -> assertWithMessage("Events inserted are not the same as received")
                        .that(allEvents)
                        .isEqualTo(insertedEvents)
        );
    }
}
