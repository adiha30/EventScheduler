package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.services.Endpoints.EventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.adiha.EventScheduler.utils.Constants.CREATION_TIME;

/**
 * Controller for handling requests related to events.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventsController {
    private final EventsService eventsService;

    /**
     * Get all events, optionally sorted by a specified field and order.
     *
     * @param sort  the field to sort by
     * @param order the order to sort by (ASC or DESC)
     * @return a list of all events
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(value = "sort", required = false, defaultValue = CREATION_TIME) String sort,
                                                    @RequestParam(value = "direction", required = false, defaultValue = "ASC") String order) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(eventsService.getAll(sort, order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get an event by its ID.
     *
     * @param eventId the ID of the event
     * @return the event with the specified ID
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable(value = "eventId") String eventId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(eventsService.getEventById(eventId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get events by location and venue, optionally sorted by a specified field and order.
     *
     * @param location the location to filter by
     * @param venue    the venue to filter by
     * @param sort     the field to sort by
     * @param order    the order to sort by (ASC or DESC)
     * @return a list of events that match the specified location and venue
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/filter")
    public ResponseEntity<List<Event>> getEventsByLocationAndVenue(
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "venue", required = false) String venue,
            @RequestParam(value = "sort", required = false, defaultValue = CREATION_TIME) String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String order) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(eventsService.getEventsByLocationAndVenue(location, venue, sort, order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Create a new event.
     *
     * @param event the event to create
     * @return the created event
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@Validated @RequestBody Event event) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(eventsService.createEvent(event));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Create multiple new events.
     *
     * @param events the events to create
     * @return the created events
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/multiple")
    public ResponseEntity<List<Event>> createEvents(@Validated @RequestBody List<Event> events) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(eventsService.createAll(events));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update an existing event.
     *
     * @param eventId the ID of the event to update
     * @param event   the new event data
     * @return the updated event
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/events/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable(value = "eventId") String eventId, @Validated @RequestBody Event event) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(eventsService.updateEvent(eventId, event));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to update event", e);
        }
    }

    /**
     * Update multiple existing events.
     *
     * @param events the new event data
     * @return the updated events
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/events/multiple")
    public ResponseEntity<List<Event>> updateEvents(@Validated @RequestBody List<Event> events) {
        try {
            List<Event> updatedEvents = new ArrayList<>();

            events.forEach((Event event) ->
                    updatedEvents.add(eventsService.updateEvent(event.getEventId(), event))
            );

            return ResponseEntity.status(HttpStatus.OK).body(updatedEvents.stream().filter(Objects::nonNull).toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to update event", e);
        }

    }

    /**
     * Delete an event by its ID.
     *
     * @param eventId the ID of the event to delete
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(@PathVariable(value = "eventId") String eventId) {
        try {
            eventsService.deleteEvent(eventId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete event", e);
        }
    }

    /**
     * Delete multiple events by their IDs.
     *
     * @param eventIds the IDs of the events to delete
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/events/multiple")
    public void deleteEvents(@RequestParam(value = "eventIds") List<String> eventIds) {
        try {
            eventsService.deleteAll(eventIds);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete events", e);
        }
    }
}