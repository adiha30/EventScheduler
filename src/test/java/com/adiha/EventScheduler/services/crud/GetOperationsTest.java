package com.adiha.EventScheduler.services.crud;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.services.Endpoints.EventsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.adiha.EventScheduler.TestUtils.*;
import static com.adiha.EventScheduler.utils.Constants.*;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
<<<<<<< Updated upstream
@TestPropertySource(locations="classpath:application-test.properties")
=======
@TestPropertySource(locations= "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
>>>>>>> Stashed changes
public class GetOperationsTest {

    @Autowired
    private EventsService sut; /** System Under Test */

    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("Test get all events with default sorting")
    @Transactional
    void testGetAllEventsDefaultSorting() {
        Event event1 = getSimpleEvent();
        Event event2 = getSimpleEvent();
        List<Event> insertedEvents = List.of(event1, event2);
        eventRepository.saveAll(insertedEvents);

        List<Event> allEvents = sut.getAll(CREATION_TIME, ASCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not the same as all events' size")
                        .that(insertedEvents.size())
                        .isEqualTo(allEvents.size()),
                () -> assertWithMessage("Events inserted are not the same as received")
                        .that(allEvents)
                        .containsExactlyElementsIn(insertedEvents),
                () -> assertWithMessage("Events are not sorted by creation time in ascending order")
                        .that(allEvents.get(0).getCreationTime())
                        .isAtMost(allEvents.get(1).getCreationTime())
        );
    }

    @Test
    @DisplayName("Test get all events with sorting of creation time in descending order")
    @Transactional
    void testGetAllEventsWithSortingByCreationTimeDescending() {
        Event event1 = getSimpleEvent();
        Event event2 = getSimpleEvent();
        List<Event> insertedEvents = List.of(event1, event2);
        eventRepository.saveAll(insertedEvents);

        List<Event> allEvents = sut.getAll(CREATION_TIME, DESCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not the same as all events' size")
                        .that(insertedEvents.size())
                        .isEqualTo(allEvents.size()),
                () -> assertWithMessage("Events inserted are not the same as received")
                        .that(allEvents)
                        .containsExactlyElementsIn(insertedEvents),
                () -> assertWithMessage("Events are not sorted by creation time in descending order")
                        .that(allEvents.get(0).getCreationTime())
                        .isAtLeast(allEvents.get(1).getCreationTime())
        );
    }

    @Test
    @DisplayName("Test get all events with sorting of start time in ascending order")
    @Transactional
    void testGetAllEventsWithSortingByStartTime() {
        Event event1 = getSimpleEvent(LocalDateTime.now());
        Event event2 = getSimpleEvent(LocalDateTime.now().plusHours(1));
        List<Event> insertedEvents = List.of(event1, event2);
        eventRepository.saveAll(insertedEvents);

        List<Event> allEvents = sut.getAll(START_TIME, ASCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not the same as all events' size")
                        .that(insertedEvents.size())
                        .isEqualTo(allEvents.size()),
                () -> assertWithMessage("Events inserted are not the same as received")
                        .that(allEvents)
                        .containsExactlyElementsIn(insertedEvents),
                () -> assertWithMessage("Events are not sorted by start time in ascending order")
                        .that(allEvents.get(0).getStartTime())
                        .isAtMost(allEvents.get(1).getStartTime())
        );
    }

    @Test
    @DisplayName("Test get all events with sorting of start time in descending order")
    @Transactional
    void testGetAllEventsWithSortingByStartTimeDescending() {
        Event event1 = getSimpleEvent(LocalDateTime.now());
        Event event2 = getSimpleEvent(LocalDateTime.now().plusHours(1));
        List<Event> insertedEvents = List.of(event1, event2);
        eventRepository.saveAll(insertedEvents);

        List<Event> allEvents = sut.getAll(START_TIME, DESCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not the same as all events' size")
                        .that(insertedEvents.size())
                        .isEqualTo(allEvents.size()),
                () -> assertWithMessage("Events inserted are not the same as received")
                        .that(allEvents)
                        .containsExactlyElementsIn(insertedEvents),
                () -> assertWithMessage("Events are not sorted by start time in descending order")
                        .that(allEvents.get(0).getStartTime())
                        .isAtLeast(allEvents.get(1).getStartTime())
        );
    }

    @Test
    @DisplayName("Test get all events with sorting of popularity in ascending order")
    @Transactional
    void testGetAllEventsWithSortingByPopularity() {
        Event event1 = getSimpleEvent(Set.of("1", "2"));
        Event event2 = getSimpleEvent(Set.of("1"));
        List<Event> insertedEvents = List.of(event1, event2);
        eventRepository.saveAll(insertedEvents);

        List<Event> allEvents = sut.getAll(POPULARITY, ASCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not the same as all events' size")
                        .that(insertedEvents.size())
                        .isEqualTo(allEvents.size()),
                () -> assertWithMessage("Events inserted are not the same as received")
                        .that(allEvents)
                        .containsExactlyElementsIn(insertedEvents),
                () -> assertWithMessage("Events are not sorted by popularity in ascending order")
                        .that(allEvents.get(0).getSubscribers().size())
                        .isAtMost(allEvents.get(1).getSubscribers().size())
        );
    }

    @Test
    @DisplayName("Test get all events with sorting of popularity in descending order")
    @Transactional
    void testGetAllEventsWithSortingByPopularityDescending() {
        Event event1 = getSimpleEvent(Set.of("1", "2"));
        Event event2 = getSimpleEvent(Set.of("1"));
        List<Event> insertedEvents = List.of(event1, event2);
        eventRepository.saveAll(insertedEvents);

        List<Event> allEvents = sut.getAll(POPULARITY, DESCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not the same as all events' size")
                        .that(insertedEvents.size())
                        .isEqualTo(allEvents.size()),
                () -> assertWithMessage("Events inserted are not the same as received")
                        .that(allEvents)
                        .containsExactlyElementsIn(insertedEvents),
                () -> assertWithMessage("Events are not sorted by popularity in descending order")
                        .that(allEvents.get(0).getSubscribers().size())
                        .isAtLeast(allEvents.get(1).getSubscribers().size())
        );
    }

    @Test
    @DisplayName("Test get all events with sorting of null and order of null")
    @Transactional
    void testGetAllEventsWithSortingOfNullAndOrderOfNull() {
        Event event1 = getSimpleEvent();
        Event event2 = getSimpleEvent();
        List<Event> insertedEvents = List.of(event1, event2);
        eventRepository.saveAll(insertedEvents);

        assertThrows(IllegalArgumentException.class, () -> sut.getAll(null, null));
    }

    @Test
    @DisplayName("Test get event by id")
    @Transactional
    void testGetEventById() {
        Event event = getSimpleEvent();
        eventRepository.save(event);

        Event receivedEvent = sut.getEventById(event.getEventId());

        Assertions.assertAll(
                () -> assertWithMessage("Event retrieved is null")
                        .that(receivedEvent)
                        .isNotNull(),
                () -> assertWithMessage("Event retrieved is not the same as inserted")
                        .that(receivedEvent)
                        .isEqualTo(event)
        );
    }

    @Test
    @DisplayName("Test get event by location and venue with default sorting and order")
    @Transactional
    void testGetEventsByLocationAndVenueDefaultSortingAndOrder() {
        Event event1 = getSimpleEvent(VENUE1, LOCATION1);
        Event event2 = getSimpleEvent(VENUE1, LOCATION2);
        Event event3 = getSimpleEvent(VENUE2, LOCATION1);
        Event event4 = getSimpleEvent(VENUE2, LOCATION2);
        List<Event> insertedEvents = List.of(event1, event2, event3, event4);
        eventRepository.saveAll(insertedEvents);

        List<Event> receivedEvents = sut.getEventsByLocationAndVenue(LOCATION1, VENUE1, CREATION_TIME, ASCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not as filtered events' size should be")
                        .that(1)
                        .isEqualTo(receivedEvents.size()),
                () -> assertWithMessage("Event retrieved after filter is not the same as should be")
                        .that(receivedEvents)
                        .containsExactlyElementsIn(List.of(event1))
        );
    }

    @Test
    @DisplayName("Test get event by location only with default sorting and order")
    @Transactional
    void testGetEventsByLocationOnlyDefaultSortingAndOrder() {
        Event event1 = getSimpleEvent(VENUE1, LOCATION1);
        Event event2 = getSimpleEvent(VENUE1, LOCATION2);
        Event event3 = getSimpleEvent(VENUE2, LOCATION1);
        Event event4 = getSimpleEvent(VENUE2, LOCATION2);
        List<Event> insertedEvents = List.of(event1, event2, event3, event4);
        eventRepository.saveAll(insertedEvents);

        List<Event> receivedEvents = sut.getEventsByLocationAndVenue(LOCATION1, null, CREATION_TIME, ASCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not as filtered events' size should be")
                        .that(2)
                        .isEqualTo(receivedEvents.size()),
                () -> assertWithMessage("Event retrieved after filter is not the same as should be")
                        .that(receivedEvents)
                        .containsExactlyElementsIn(List.of(event1, event3))
        );
    }

    @Test
    @DisplayName("Test get event by venue only with default sorting and order")
    @Transactional
    void testGetEventsByVenueOnlyDefaultSortingAndOrder() {
        Event event1 = getSimpleEvent(VENUE1, LOCATION1);
        Event event2 = getSimpleEvent(VENUE1, LOCATION2);
        Event event3 = getSimpleEvent(VENUE2, LOCATION1);
        Event event4 = getSimpleEvent(VENUE2, LOCATION2);
        List<Event> insertedEvents = List.of(event1, event2, event3, event4);
        eventRepository.saveAll(insertedEvents);

        List<Event> receivedEvents = sut.getEventsByLocationAndVenue(null, VENUE1, CREATION_TIME, ASCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not as filtered events' size should be")
                        .that(2)
                        .isEqualTo(receivedEvents.size()),
                () -> assertWithMessage("Event retrieved after filter is not the same as should be")
                        .that(receivedEvents)
                        .containsExactlyElementsIn(List.of(event1, event2))
        );
    }

    @Test
    @DisplayName("Test get event by location and venue with sorting of creation time in descending order")
    @Transactional
    void testGetEventsByLocationAndVenueWithSortingByCreationTimeDescending() {
        Event event1 = getSimpleEvent(VENUE1, LOCATION1);
        Event event2 = getSimpleEvent(VENUE1, LOCATION2);
        Event event3 = getSimpleEvent(VENUE2, LOCATION1);
        Event event4 = getSimpleEvent(VENUE2, LOCATION2);
        List<Event> insertedEvents = List.of(event1, event2, event3, event4);
        eventRepository.saveAll(insertedEvents);

        List<Event> receivedEvents = sut.getEventsByLocationAndVenue(LOCATION1, VENUE1, CREATION_TIME, DESCENDING);

        Assertions.assertAll(
                () -> assertWithMessage("Size of inserted events is not as filtered events' size should be")
                        .that(1)
                        .isEqualTo(receivedEvents.size()),
                () -> assertWithMessage("Event retrieved after filter is not the same as should be")
                        .that(receivedEvents)
                        .containsExactlyElementsIn(List.of(event1))
        );
    }
}
