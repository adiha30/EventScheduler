package com.adiha.EventScheduler.integration;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.models.User;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static com.adiha.EventScheduler.integration.TestUtils.*;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Testcontainers
@ActiveProfiles("test")
public class SubscriptionsTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    private static User testUser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Authentication authentication;

    @BeforeAll
    static void setUp() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @AfterAll
    static void tearDown() {
        postgresContainer.stop();
    }

    @BeforeEach
    void beforeEach() {
        testUser = getSimpleUser();
        userRepository.save(testUser);

        when(authentication.getName()).thenReturn(TEST_USER);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Test subscribe to event")
    @Transactional
    void subscribeToEvent() throws Exception {
        Event event = createSampleEvent("Event to Subscribe", testUser.getUserId());
        eventRepository.save(event);

        mockMvc.perform(post("/api/v1/subscribe/{eventId}", event.getEventId()))
                .andExpect(status().isOk());

        Optional<Event> subscribedEvent = eventRepository.findById(event.getEventId());
        Assertions.assertAll(
                () -> assertWithMessage("event does not exist")
                        .that(subscribedEvent.isPresent())
                        .isTrue(),
                () -> assertWithMessage("user didnt subscribe to event")
                        .that(subscribedEvent.get().getSubscribers())
                        .contains(testUser.getUserId()));


    }

    @Test
    @DisplayName("Test subscribe to non-existing event")
    @Transactional
    void subscribeToNonExistingEvent() throws Exception {
        mockMvc.perform(post("/api/v1/subscribe/{eventId}", "non-existing-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test unsubscribe from event")
    @Transactional
    void unsubscribeFromEvent() throws Exception {
        Event event = createSampleEvent("Event to Unsubscribe", testUser.getUserId());
        event.addToSubscribers(testUser.getUserId());
        eventRepository.save(event);

        mockMvc.perform(post("/api/v1/unsubscribe/{eventId}", event.getEventId()))
                .andExpect(status().isOk());

        Optional<Event> unsubscribedEvent = eventRepository.findById(event.getEventId());
        Assertions.assertAll(
                () -> assertWithMessage("event does not exist")
                        .that(unsubscribedEvent.isPresent())
                        .isTrue(),
                () -> assertWithMessage("user didnt unsubscribe from event")
                        .that(unsubscribedEvent.get().getSubscribers())
                        .doesNotContain(testUser.getUserId())
        );
    }

    @Test
    @DisplayName("Test unsubscribe from non-existing event")
    @Transactional
    void unsubscribeFromNonExistingEvent() throws Exception {
        mockMvc.perform(post("/api/v1/unsubscribe/{eventId}", "non-existing-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test unsubscribe when user is not subscribed")
    @Transactional
    void unsubscribeWhenUserNotSubscribed() throws Exception {
        Event event = createSampleEvent("Event to Unsubscribe", testUser.getUserId());
        eventRepository.save(event);

        mockMvc.perform(post("/api/v1/unsubscribe/{eventId}", event.getEventId()))
                .andExpect(status().isNotFound());

        Optional<Event> unsubscribedEvent = eventRepository.findById(event.getEventId());
        Assertions.assertAll(
                () -> assertWithMessage("event does not exist")
                        .that(unsubscribedEvent.isPresent())
                        .isTrue(),
                () -> assertWithMessage("user should not be subscribed")
                        .that(unsubscribedEvent.get().getSubscribers())
                        .isNull()
        );
    }
}
