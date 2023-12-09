package com.adiha.EventScheduler.utils;

/**
 * Constants class.
 * This class contains constant values used throughout the application.
 */
public class Constants {
    // Ordering
    public static final String CREATION_TIME = "creationTime";
    public static final String START_TIME = "startTime";
    public static final String POPULARITY = "popularity";

    // Sorting
    public static final String ASCENDING = "ASC";
    public static final String DESCENDING = "DESC";

    // Websocket
    public static final String WEB_SOCKET_ENDPOINT = "ws";
    public static final String WEBSOCKET_PREFIX = "/app";
    public static final String TOPICS_DEST_PREFIX = "/topic";
    public static final String EVENTS_TOPIC = "/events/";
    public static final String EVENTS_UPDATES_TOPIC = "/updates";
}
