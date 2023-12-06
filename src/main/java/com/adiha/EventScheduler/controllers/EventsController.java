package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.utils.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventsController {
    private final Logger logger = LoggerFactory.getLogger(EventsController.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        logger.debug("Retrieving all events");

        return eventRepository.findAll();
    }

    @GetMapping("/events/{eventId}")
    public Event getEventById(@PathVariable(value = "eventId") UUID eventId) {
        logger.debug("Retrieving event with id: {}", eventId);

        return eventRepository.findById(eventId).orElseThrow(() -> throwNotFoundException(eventId));
    }

    @PostMapping("/events")
    public Event createEvent(@Validated @RequestBody Event event) {
        logger.debug("Creating event: {}", event);

        return eventRepository.save(event);
    }

    @PatchMapping("/events/{eventId}")
    public Event updateEvent(@PathVariable(value = "eventId") UUID eventId, @Validated @RequestBody Event event) {
        logger.debug("Updating event with id: {}", eventId);

        Event updatedEvent = eventRepository.findById(eventId)
                .map((Event eventToUpdate) -> updateEvent(event, eventToUpdate))
                .orElseThrow(() -> throwNotFoundException(eventId));

        return eventRepository.save(updatedEvent);
    }

    @DeleteMapping("/events/{eventId}")
    public Event deleteEvent(@PathVariable(value = "eventId") UUID eventId) {
        logger.debug("Deleting event with id: {}", eventId);

        return eventRepository.findById(eventId)
                .map(this::deleteAndReturn)
                .orElseThrow(() -> throwNotFoundException(eventId));
    }
    
    private static ResponseStatusException throwNotFoundException(UUID eventId) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Event with uuid %s was not found", eventId));
    }

    private Event deleteAndReturn(Event event) {
        eventRepository.delete(event);

        return event;
    }

    private Event updateEvent(Event event, Event eventToUpdate) {
        eventMapper.updateEventFromDto(event, eventToUpdate);

        return eventToUpdate;
    }
}
