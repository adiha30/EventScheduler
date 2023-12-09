package com.adiha.EventScheduler.services.reminders;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service class for scheduling tasks.
 */
@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    /**
     * Cron expression for scheduling tasks to run at the start of every minute.
     */
    public static final String EVERY_START_OF_MINUTE = "0 * * * * *";

    /**
     * Service for sending reminders.
     */
    private final ReminderService reminderService;

    /**
     * Scheduled task for sending reminders.
     * This task is scheduled to run at the start of every minute.
     */
    @Scheduled(cron = EVERY_START_OF_MINUTE)
    public void sendReminders() {
        reminderService.sendReminders();
    }
}