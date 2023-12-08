package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.services.EventsService;
import com.adiha.EventScheduler.utils.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.adiha.EventScheduler.utils.mapper.Constants.CREATION_TIME;

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

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/events/{eventId}")
    public Event updateEvent(@PathVariable(value = "eventId") String eventId, @Validated @RequestBody Event event) {
        return eventsService.updateEvent(eventId, event);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(@PathVariable(value = "eventId") String eventId) {
        eventsService.deleteEvent(eventId);
    }
}
