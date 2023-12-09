package com.adiha.EventScheduler.services.crud;

import com.adiha.EventScheduler.expections.UserNotAuthorized;
import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.models.User;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.repositories.UserRepository;
import com.adiha.EventScheduler.services.Endpoints.EventsService;
import com.google.common.truth.Truth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.adiha.EventScheduler.TestUtils.getSimpleEvent;
import static com.adiha.EventScheduler.TestUtils.getSimpleUser;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class DeleteOperationsTest {

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
                UserNotAuthorized.class,
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
