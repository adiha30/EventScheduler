package com.adiha.EventScheduler.services.subscriptions;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.models.User;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.repositories.UserRepository;
import com.adiha.EventScheduler.services.Endpoints.SubscriptionService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.adiha.EventScheduler.TestUtils.getSimpleEvent;
import static com.adiha.EventScheduler.TestUtils.getSimpleUser;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class SubscriptionOperationsTest {

    public static final String NOT_EXISTING_EVENT_ID = "NOT_EXISTING_EVENT_ID";
    @Autowired
    private SubscriptionService sut;
    /**
     * System Under Test
     */

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
    @DisplayName("subscribe() should add the user to the event's subscribers")
    @Transactional
    void subscribeToAddUserToEventSubscribers() {
        Event eventToSub = getSimpleEvent();
        eventRepository.save(eventToSub);

        sut.subscribe(eventToSub.getEventId());

        Truth.assertThat(eventRepository.findById(eventToSub.getEventId()).get().getSubscribers())
                .contains(testUser.getUserId());
    }

    @Test
    @DisplayName("subscribe() should throw an exception if the event does not exist")
    @Transactional
    void subscribeToThrowExceptionIfEventDoesNotExist() {
        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> sut.subscribe(NOT_EXISTING_EVENT_ID));
    }

    @Test
    @DisplayName("unsubscribe() should remove the user from the event's subscribers")
    @Transactional
    void unsubscribeToRemoveUserFromEventSubscribers() {
        Event eventToUnsub = getSimpleEvent();
        eventToUnsub.addToSubscribers(testUser.getUserId());
        eventRepository.save(eventToUnsub);

        sut.unsubscribe(eventToUnsub.getEventId());

        Truth.assertThat(eventRepository.findById(eventToUnsub.getEventId()).get().getSubscribers())
                .doesNotContain(testUser.getUserId());
    }

    @Test
    @DisplayName("unsubscribe() should throw an exception if the event does not exist")
    @Transactional
    void unsubscribeToThrowExceptionIfEventDoesNotExist() {
        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> sut.unsubscribe(NOT_EXISTING_EVENT_ID));
    }

    @Test
    @DisplayName("unsubscribe() should throw an exception if the user is not subscribed to the event")
    @Transactional
    void unsubscribeToThrowExceptionIfUserIsNotSubscribedToEvent() {
        Event eventToUnsub = getSimpleEvent();
        eventRepository.save(eventToUnsub);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> sut.unsubscribe(eventToUnsub.getEventId()));
    }
}