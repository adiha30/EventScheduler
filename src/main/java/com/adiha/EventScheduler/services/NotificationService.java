package com.adiha.EventScheduler.services;

import com.adiha.EventScheduler.models.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for handling notifications related to events.
 */
@Service
public class NotificationService {
    private final Logger logger = LoggerFactory.getLogger(EventsService.class);

    /**
     * Sends a reminder for the specified event.
     *
     * @param event The event for which to send a reminder.
     */
    public void sendReminder(Event event) {
        logger.info("{} is starting soon!", event.getName());
    }
}