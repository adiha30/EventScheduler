package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.services.EventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.adiha.EventScheduler.utils.Constants.CREATION_TIME;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventsController {
    private final EventsService eventsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events")
    public List<Event> getAllEvents(@RequestParam(value = "sort", required = false, defaultValue = CREATION_TIME) String sort,
                                    @RequestParam(value = "direction", required = false, defaultValue = "ASC") String order) {
        return eventsService.getAll(sort, order);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/{eventId}")
    public Event getEventById(@PathVariable(value = "eventId") String eventId) {
        return eventsService.getEventById(eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/filter")
    public List<Event> getEventsByLocationAndVenue(
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "venue", required = false) String venue,
            @RequestParam(value = "sort", required = false, defaultValue = CREATION_TIME) String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String order) {
        return eventsService.getEventsByLocationAndVenue(location, venue, sort, order);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public Event createEvent(@Validated @RequestBody Event event) {
        return eventsService.createEvent(event);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/multiple")
    public List<Event> createEvents(@Validated @RequestBody List<Event> events) {
        return eventsService.createEvents(events);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/events/{eventId}")
    public Event updateEvent(@PathVariable(value = "eventId") String eventId, @Validated @RequestBody Event event) {
        return eventsService.updateEvent(eventId, event);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/events/multiple")
    public List<Event> updateEvents(@Validated @RequestBody List<Event> events) {
        List<Event> updatedEvents = new ArrayList<>();

        events.forEach((Event event) ->
                updatedEvents.add(eventsService.updateEvent(event.getEventId(), event))
        );

        return updatedEvents;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(@PathVariable(value = "eventId") String eventId) {
        eventsService.deleteEvent(eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/events/multiple")
    public void deleteEvents(@RequestParam(value = "eventIds") List<String> eventIds) {
        eventsService.deleteAll(eventIds);
    }
}
