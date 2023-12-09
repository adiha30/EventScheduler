package com.adiha.EventScheduler.services.crud;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.adiha.EventScheduler.TestUtils.getSimpleEvent;
import static com.adiha.EventScheduler.TestUtils.getSimpleUser;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
<<<<<<< Updated upstream
@TestPropertySource(locations = "classpath:application-test.properties")
=======
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
>>>>>>> Stashed changes
public class UpdateOperationsTest {

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
