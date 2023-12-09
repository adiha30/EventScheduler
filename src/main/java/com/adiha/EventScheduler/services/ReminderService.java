package com.adiha.EventScheduler.services;

import com.adiha.EventScheduler.models.Event;
import com.adiha.EventScheduler.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    public static final int REMINDER_TIME = 30;
    private final EventRepository eventRepository;
    private final NotificationService notificationService;

    public void sendReminders() {
        getUpcomingEvents().forEach(this::notifyAndSetSent);
    }

    private void notifyAndSetSent(Event event) {
        notificationService.sendReminder(event);
        event.setReminderSent(true);
        eventRepository.save(event);
    }

    private List<Event> getUpcomingEvents() {
        return eventRepository.findByStartTimeBetweenAndReminderSentIsFalse(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(REMINDER_TIME));
    }


}
