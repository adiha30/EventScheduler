package com.adiha.EventScheduler.services;

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
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.adiha.EventScheduler.utils.Constants.*;

/**
 * Service class for handling operations related to events.
 */
@Service
@RequiredArgsConstructor
public class EventsService {
    private final Logger logger = LoggerFactory.getLogger(EventsService.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private static Specification<Event> getSpecification(String location, String venue) {
        return Specification
                .where(new EventByLocation(location))
                .and(new EventByVenue(venue));
    }

    private static ResponseStatusException throwNotFoundException(String eventId) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Event with uuid %s was not found", eventId));
    }

    /**
     * Retrieves all events, optionally sorted by a specified field and order.
     *
     * @param sort  the field to sort by
     * @param order the order to sort by (ASC or DESC)
     * @return a list of all events
     */
    public List<Event> getAll(String sort, String order) {
        logger.debug("Retrieving all events");

        if (POPULARITY.equals(sort)) {
            return getEventSortedBy(order);
        }

        Sort sortingParameters = getSortingParameters(sort, order);

        return eventRepository.findAll(sortingParameters);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the ID of the event
     * @return the event with the specified ID
     */
    public Event getEventById(String eventId) {
        logger.debug("Retrieving event with id: {}", eventId);

        return eventRepository.findById(eventId).orElseThrow(() -> throwNotFoundException(eventId));
    }

    /**
     * Retrieves events by location and venue, optionally sorted by a specified field and order.
     *
     * @param location the location to filter by
     * @param venue    the venue to filter by
     * @param sort     the field to sort by
     * @param order    the order to sort by (ASC or DESC)
     * @return a list of events that match the specified location and venue
     */
    public List<Event> getEventsByLocationAndVenue(
            String location,
            String venue,
            String sort,
            String order) {
        logger.debug("Retrieving events with location: {} and venue: {}", location, venue);

        Specification<Event> spec = getSpecification(location, venue);

        if (POPULARITY.equals(sort)) {
            return getEventSortedBy(order);
        }

        Sort sortingParameters = getSortingParameters(sort, order);

        return eventRepository.findAll(spec, sortingParameters);
    }

    /**
     * Creates a new event.
     *
     * @param event the event to create
     * @return the created event
     */
    public Event createEvent(Event event) {
        logger.debug("Creating event: {}", event);

        return eventRepository.save(event);
    }

    /**
     * Creates multiple new events.
     *
     * @param events the events to create
     * @return the created events
     */
    public List<Event> createAll(List<Event> events) {
        return eventRepository.saveAll(events);
    }

    /**
     * Updates an existing event.
     *
     * @param eventId the ID of the event to update
     * @param event   the new event data
     * @return the updated event
     */
    public Event updateEvent(String eventId, Event event) {
        logger.debug("Updating event with id: {}", eventId);

        Event updatedEvent = eventRepository.findById(eventId)
                .map((Event eventToUpdate) -> updateEvent(event, eventToUpdate))
                .orElseThrow(() -> throwNotFoundException(eventId));

        return eventRepository.save(updatedEvent);
    }

    /**
     * Deletes an event by its ID.
     *
     * @param eventId the ID of the event to delete
     */
    public void deleteEvent(String eventId) {
        logger.debug("Deleting event with id: {}", eventId);

        if (eventId == null) {
            throw new IllegalArgumentException("Event id cannot be null");
        } else if (eventRepository.findById(eventId).isPresent()) {
            eventRepository.deleteById(eventId);
            return;
        }

        throwNotFoundException(eventId);
    }

    /**
     * Deletes multiple events by their IDs.
     *
     * @param eventIds the IDs of the events to delete
     */
    public void deleteAll(List<String> eventIds) {
        logger.debug("Deleting events with ids: {}", eventIds);

        List<Event> eventsToDelete = eventRepository.findAllById(eventIds);

        eventRepository.deleteAll(eventsToDelete);
    }

    private List<Event> getEventSortedBy(String order) {
        return DESCENDING.equals(order)
                ? eventRepository.findAllOrderByPopularityDesc()
                : eventRepository.findAllOrderByPopularityAsc();
    }

    private Sort getSortingParameters(String sort, String order) {
        if (sort == null || sortNotAllowed(sort)) {
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