package com.adiha.EventScheduler.unit.services.crud;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.models.User;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.repositories.UserRepository;
import com.adiha.EventScheduler.services.Endpoints.EventsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.adiha.EventScheduler.unit.TestUtils.getSimpleEvent;
import static com.adiha.EventScheduler.unit.TestUtils.getSimpleUser;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class PostOperationsTest {

    @Autowired
    private EventsService sut;

    @Autowired
    private EventRepository eventRepository;

    private User testUser;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void authMock() {
        testUser = getSimpleUser();

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
    }

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

    @Test
    @DisplayName("Test create multiple events")
    @Transactional
    void testCreateMultipleEvents() {
        Event event1 = getSimpleEvent();
        Event event2 = getSimpleEvent();
        List<Event> eventsToInsert = List.of(event1, event2);

        List<Event> eventsInserted = sut.createAll(eventsToInsert);

        Assertions.assertAll(
                () -> assertWithMessage("Created events are null")
                        .that(eventsInserted)
                        .isNotNull(),
                () -> assertWithMessage("Events were not inserted")
                        .that(eventRepository.findAll())
                        .containsExactlyElementsIn(eventsToInsert)
        );
    }

    @Test
    @DisplayName("Test create multiple events with null events")
    @Transactional
    void testCreateMultipleEventsWithNullEvents() {
        Assertions.assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> sut.createAll(null)
        );
    }

}
