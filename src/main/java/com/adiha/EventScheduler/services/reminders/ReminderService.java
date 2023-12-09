package com.adiha.EventScheduler.services.reminders;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for handling reminders related to events.
 */
@Service
@RequiredArgsConstructor
public class ReminderService {

    /**
     * The time in minutes before an event starts when a reminder should be sent.
     */
    public static final int REMINDER_TIME = 30;

    /**
     * Repository for accessing event data.
     */
    private final EventRepository eventRepository;

    /**
     * Service for sending notifications.
     */
    private final NotificationService notificationService;

    /**
     * Sends reminders for all upcoming events.
     */
    public void sendReminders() {
        getUpcomingEvents().forEach(this::notifyAndSetSent);
    }

    /**
     * Sends a reminder for the specified event and marks the reminder as sent.
     *
     * @param event The event for which to send a reminder.
     */
    private void notifyAndSetSent(Event event) {
        notificationService.sendReminder(event);
        event.setReminderSent(true);
        eventRepository.save(event);
    }

    /**
     * Finds all events that start within the next REMINDER_TIME minutes and for which a reminder has not been sent.
     *
     * @return A list of events that start within the next REMINDER_TIME minutes and for which a reminder has not been sent.
     */
    private List<Event> getUpcomingEvents() {
        return eventRepository.findByStartTimeBetweenAndReminderSentIsFalse(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(REMINDER_TIME));
    }
}