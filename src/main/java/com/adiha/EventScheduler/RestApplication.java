package com.adiha.EventScheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Event Scheduler REST API.
 * This class contains the main method which starts the Spring Boot application.
 */
@SpringBootApplication
public class RestApplication {

    /**
     * Main method which serves as the entry point for the application.
     * This method delegates to Spring Boot's SpringApplication class, which bootstraps the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

}