package com.adiha.EventScheduler.services.Endpoints;

import com.adiha.EventScheduler.expections.UserNotAuthorized;
import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.models.enums.UpdateType;
import com.adiha.EventScheduler.models.websocket.EventUpdate;
import com.adiha.EventScheduler.repositories.EventRepository;
import com.adiha.EventScheduler.repositories.UserRepository;
import com.adiha.EventScheduler.specifications.EventByLocation;
import com.adiha.EventScheduler.specifications.EventByVenue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.adiha.EventScheduler.utils.Constants.*;

/**
 * Service class for handling operations related to events.
 */
@Service
public class EventsService extends CrudService {
    private final Logger logger = LoggerFactory.getLogger(EventsService.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final EventRepository eventRepository;

    /**
     * Constructor for the EventsService class.
     * It initializes the userRepository, eventRepository, and eventMapper.
     *
     * @param userRepository  The UserRepository to handle the database operations related to the User entity.
     * @param eventRepository The EventRepository to handle the database operations related to the Event entity.
     */
    public EventsService(UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate, EventRepository eventRepository) {
        super(userRepository);
        this.messagingTemplate = simpMessagingTemplate;
        this.eventRepository = eventRepository;
    }

    private static Specification<Event> getSpecification(String location, String venue) {
        return Specification
                .where(new EventByLocation(location))
                .and(new EventByVenue(venue));
    }

    private static String getDestinationTopic(Event event) {
        return TOPICS_DEST_PREFIX + EVENTS_TOPIC + event.getEventId() + EVENTS_UPDATES_TOPIC;
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
        if (event == null) {
            logger.error("Event inserted was null");
            throw new InvalidDataAccessApiUsageException("Event cannot be null");
        }

        logger.debug("Creating event: {}", event);

        setCreatingUserAndSubscribers(event);

        return eventRepository.save(event);
    }

    /**
     * Creates multiple new events.
     *
     * @param events the events to create
     * @return the created events
     */
    public List<Event> createAll(List<Event> events) {
        if (events == null) {
            logger.error("Events inserted were null");

            throw new InvalidDataAccessApiUsageException("Event cannot be null");
        }

        logger.debug("Creating events: {}", events);

        events.forEach(this::setCreatingUserAndSubscribers);

        return eventRepository.saveAll(events);
    }

    /**
     * Updates an existing event and sends updates via websockets to all subscribers of it.
     *
     * @param eventId      the ID of the event to update
     * @param updatedEvent the new event data
     * @return the updated event
     */
    public Event updateEvent(String eventId, Event updatedEvent) {
        logger.debug("Updating event with id: {}", eventId);

        Event eventAfterUpdate = eventRepository.findById(eventId)
                .map((Event eventToUpdate) -> updateEvent(updatedEvent, eventToUpdate))
                .orElseThrow(() -> throwNotFoundException(eventId));
        Event savedEvent = eventRepository.save(eventAfterUpdate);

        sendEventUpdate(savedEvent, UpdateType.UPDATED);

        return savedEvent;
    }

    /**
     * Deletes an event by its ID and sends updates via websockets to all subscribers of it.
     *
     * @param eventId the ID of the event to delete
     */
    public void deleteEvent(String eventId) {
        logger.debug("Deleting event with id: {}", eventId);

        if (eventId == null) {
            throw new IllegalArgumentException("Event id cannot be null");
        } else if (isAuthorizedToDelete(eventId)) {
            var eventToDelete = eventRepository.findById(eventId).orElseThrow(() -> throwNotFoundException(eventId));

            eventRepository.deleteById(eventId);

            sendEventUpdate(eventToDelete, UpdateType.CANCELED);

            return;
        }

        if (eventRepository.findById(eventId).isPresent()) {
            throw new UserNotAuthorized(String.format("User is not authorized to delete event %s", eventId));
        }

        throwNotFoundException(eventId);
    }

    private boolean isAuthorizedToDelete(String eventId) {
        boolean isPresent = eventRepository.findById(eventId).isPresent();

        return isPresent &&
                getActiveUserId().equals(eventRepository.findById(eventId).get().getCreatingUserId());
    }

    /**
     * Deletes multiple events by their IDs and sends updates via websockets to all subscribers of them.
     *
     * @param eventIds the IDs of the events to delete
     */
    public void deleteAll(List<String> eventIds) {
        logger.debug("Deleting events with ids: {}", eventIds);

        List<Event> eventsToDelete = eventRepository.findAllById(eventIds);
        eventsToDelete.forEach((Event event) -> sendEventUpdate(event, UpdateType.CANCELED));

        eventRepository.deleteAll(eventsToDelete);
    }

    private void sendEventUpdate(Event event, UpdateType updateType) {
        if (event != null) {
            String destinationTopic = getDestinationTopic(event);
            logger.info("Sending {} update for {} in topic {}",
                    updateType.getValue(),
                    event.getName(),
                    destinationTopic
            );

            var eventUpdate = EventUpdate.builder()
                    .event(event)
                    .updateType(updateType)
                    .build();

            messagingTemplate.convertAndSend(destinationTopic, eventUpdate);
        }
    }

    private void setCreatingUserAndSubscribers(Event event) {
        String creatingUserId = getActiveUserId();

        event.setCreatingUserId(creatingUserId);
        event.addToSubscribers(creatingUserId);
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

    private Event updateEvent(Event newEvent, Event oldEvent) {
        if (!getActiveUserId().equals(oldEvent.getCreatingUserId())) {
            throw new UserNotAuthorized("User is not authorized to update this event");
        }

        return fillOutEventDetails(newEvent, oldEvent);
    }

    private Event fillOutEventDetails(Event newEvent, Event oldEvent) {
        return oldEvent.toBuilder()
                .startTime(newEvent.getStartTime())
                .endTime(newEvent.getEndTime())
                .reminderSent(false)
                .name(newEvent.getName())
                .location(newEvent.getLocation())
                .venue(newEvent.getVenue())
                .build();
    }
}