package com.adiha.EventScheduler.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    public static final String EVERY_START_OF_MINUTE = "0 * * * * *";

    private final ReminderService reminderService;

    @Scheduled(cron = EVERY_START_OF_MINUTE)
    public void sendReminders() {
        reminderService.sendReminders();
    }
}
