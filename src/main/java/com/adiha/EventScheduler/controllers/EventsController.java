package com.adiha.EventScheduler.controllers;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.specifications.EventByLocation;
import com.adiha.EventScheduler.specifications.EventByVenue;
import com.adiha.EventScheduler.utils.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventsController {
    public static final String CREATION_TIME = "creationTime";
    public static final String START_TIME = "startTime";
    public static final String POPULARITY = "popularity";
    private final Logger logger = LoggerFactory.getLogger(EventsController.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private static ResponseStatusException throwNotFoundException(String eventId) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Event with uuid %s was not found", eventId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events")
    public List<Event> getAllEvents(@RequestParam(value = "sort", required = false, defaultValue = CREATION_TIME) String sort,
                                    @RequestParam(value = "direction", required = false, defaultValue = "ASC") String order) {
        logger.debug("Retrieving all events");

        if (POPULARITY.equals(sort)) {
            return eventRepository.findAllOrderByPopularity();
        }

        Sort sortingParameters = getSortingParameters(sort, order);

        return eventRepository.findAll(sortingParameters);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/{eventId}")
    public Event getEventById(@PathVariable(value = "eventId") String eventId) {
        logger.debug("Retrieving event with id: {}", eventId);

        return eventRepository.findById(eventId).orElseThrow(() -> throwNotFoundException(eventId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/filter")
    public List<Event> getEventsByLocationAndVenue(
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "venue", required = false) String venue,
            @RequestParam(value = "sort", required = false, defaultValue = CREATION_TIME) String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "ASC") String order) {
        logger.debug("Retrieving events with location: {} and venue: {}", location, venue);

        Specification<Event> spec = Specification
                .where(new EventByLocation(location))
                .and(new EventByVenue(venue));

        if (POPULARITY.equals(sort)) {
            return eventRepository.findAllOrderByPopularity(spec);
        }

        Sort sortingParameters = getSortingParameters(sort, order);

        return eventRepository.findAll(spec, sortingParameters);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public Event createEvent(@Validated @RequestBody Event event) {
        logger.debug("Creating event: {}", event);

        return eventRepository.save(event);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/events/{eventId}")
    public Event updateEvent(@PathVariable(value = "eventId") String eventId, @Validated @RequestBody Event event) {
        logger.debug("Updating event with id: {}", eventId);

        Event updatedEvent = eventRepository.findById(eventId)
                .map((Event eventToUpdate) -> updateEvent(event, eventToUpdate))
                .orElseThrow(() -> throwNotFoundException(eventId));

        return eventRepository.save(updatedEvent);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(@PathVariable(value = "eventId") String eventId) {
        logger.debug("Deleting event with id: {}", eventId);

        eventRepository.findById(eventId)
                .ifPresentOrElse(eventRepository::delete, () -> throwNotFoundException(eventId));
    }

    private Sort getSortingParameters(String sort, String order) {
        if (sortNotAllowed(sort)) {
            throw new IllegalArgumentException("Sorting can only be done on 'creationTime' or 'startDate'");
        }

        return Sort.by(Sort.Direction.fromString(order), sort);
    }

    private boolean sortNotAllowed(String sort) {
        return !sort.equals(CREATION_TIME)
                && !sort.equals(START_TIME)
                && !sort.equals(POPULARITY);
    }

    private Event updateEvent(Event event, Event eventToUpdate) {
        eventMapper.updateEventFromDto(event, eventToUpdate);

        return eventToUpdate;
    }
}
