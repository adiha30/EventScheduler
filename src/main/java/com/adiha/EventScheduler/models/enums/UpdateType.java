package com.adiha.EventScheduler.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum represents the types of updates that can occur to an event.
 * The types of updates are:
 * - UPDATED: An update that occurs when an event is modified.
 * - DELETED: An update that occurs when an event is removed.
 */
@RequiredArgsConstructor
@Getter
public enum UpdateType {
    UPDATED("Update"),
    CANCELED("Cancel");

    private final String value;
}