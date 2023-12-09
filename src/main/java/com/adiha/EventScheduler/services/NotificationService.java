package com.adiha.EventScheduler.services;

import com.adiha.EventScheduler.models.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final Logger logger = LoggerFactory.getLogger(EventsService.class);

    public void sendReminder(Event event) {
        logger.info("{} is starting soon!", event.getName());
    }
}
