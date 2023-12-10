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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.adiha.EventScheduler.integration.TestUtils.createSampleEvent;
import static com.adiha.EventScheduler.integration.TestUtils.getSimpleUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Testcontainers
@ActiveProfiles("test")
class EventsCrudTest {

    public static final String TEST_USER = "test-user";
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");
    private static User testUser;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;

    @BeforeAll
    static void setUp() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());

        testUser = getSimpleUser();
    }

    @AfterAll
    static void tearDown() {
        postgresContainer.stop();
    }

    @BeforeEach
    void mockActiveUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_USER);
        when(userRepository.findByUsername(TEST_USER)).thenReturn(Optional.ofNullable(testUser));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Test get all events")
    @Transactional
    void getAllEventsWithOkResponse() throws Exception {
        Event event1 = createSampleEvent("Event 1", testUser.getUserId());
        Event event2 = createSampleEvent("Event 2", testUser.getUserId());
        eventRepository.saveAll(List.of(event1, event2));

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Event 1"))
                .andExpect(jsonPath("$[1].name").value("Event 2"));
    }

    @Test
    @DisplayName("Test get event by id with invalid id")
    @Transactional
    void getEventByIdWithInvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/events/{eventId}", "invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test create a new event")
    @Transactional
    void createEvent() throws Exception {
        Event event = createSampleEvent("Positive Test Event", testUser.getUserId());

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Positive Test Event"))
                .andExpect(jsonPath("$.creatingUserId").value(testUser.getUserId()));
    }

    @Test
    @DisplayName("Test create event with missing required field")
    @Transactional
    void createEventWithMissingField() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date\":\"2023-12-31\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test update an existing event")
    @Transactional
    void updateEvent() throws Exception {
        Event event = createSampleEvent("Event to Update", testUser.getUserId());
        eventRepository.save(event);

        Event updatedEvent = new Event();
        updatedEvent.setName("Updated Event");
        updatedEvent.setStartTime(LocalDateTime.now().plusDays(3));

        mockMvc.perform(put("/api/v1/events/{eventId}", event.getEventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Event"))
                .andExpect(jsonPath("$.creatingUserId").value(testUser.getUserId()));
    }

    @Test
    @DisplayName("Test update event with invalid id")
    @Transactional
    void updateEventWithInvalidId() throws Exception {
        mockMvc.perform(put("/api/v1/events/{eventId}", "invalid-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Event\",\"date\":\"2023-12-31\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete an existing event")
    @Transactional
    void deleteEvent() throws Exception {
        Event event = createSampleEvent("Event to Delete", testUser.getUserId());
        eventRepository.save(event);

        mockMvc.perform(delete("/api/v1/events/{eventId}", event.getEventId()))
                .andExpect(status().isOk());

        // Verify that the event is deleted
        assertFalse(eventRepository.findById(event.getEventId()).isPresent());
    }

    @Test
    @DisplayName("Test delete event with invalid id")
    @Transactional
    void deleteEventWithInvalidId() throws Exception {
        mockMvc.perform(delete("/api/v1/events/{eventId}", "invalid-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete event with valid id")
    @Transactional
    void deleteEventWithValidId() throws Exception {
        Event event1 = createSampleEvent("Event 1", testUser.getUserId());
        eventRepository.save(event1);

        mockMvc.perform(delete("/api/v1/events/{eventId}", event1.getEventId()))
                .andExpect(status().isOk());
    }
}
