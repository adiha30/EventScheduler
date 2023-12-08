# EventScheduler
API Backend for the Event Scheduler app

1. **GET /api/v1/events**
    - Description: Retrieves all events, optionally sorted by a specified field and order.
    - Query Parameters:
        - `sort`: The field to sort by. Default is `creationTime`.
        - `direction`: The order to sort by (ASC or DESC). Default is `ASC`.
    - Response: A list of all events.


2. **GET /api/v1/events/{eventId}**
    - Description: Retrieves an event by its ID.
    - Path Parameters:
        - `eventId`: The ID of the event.
    - Response: The event with the specified ID.


3. **GET /api/v1/events/filter**
    - Description: Retrieves events by location and venue, optionally sorted by a specified field and order.
    - Query Parameters:
        - `location`: The location to filter by.
        - `venue`: The venue to filter by.
        - `sort`: The field to sort by. Default is `creationTime`.
        - `direction`: The order to sort by (ASC or DESC). Default is `ASC`.
    - Response: A list of events that match the specified location and venue.


4. **POST /api/v1/events**
    - Description: Creates a new event.
    - Request Body: The event to create.
    - Response: The created event.


5. **POST /api/v1/events/multiple**
    - Description: Creates multiple new events.
    - Request Body: The events to create.
    - Response: The created events.


6. **PUT /api/v1/events/{eventId}**
    - Description: Updates an existing event.
    - Path Parameters:
        - `eventId`: The ID of the event to update.
    - Request Body: The new event data.
    - Response: The updated event.


7. **PUT /api/v1/events/multiple**
    - Description: Updates multiple existing events.
    - Request Body: The new event data.
    - Response: The updated events.


8. **DELETE /api/v1/events/{eventId}**
    - Description: Deletes an event by its ID.
    - Path Parameters:
        - `eventId`: The ID of the event to delete.
    - Response: No content.


9. **DELETE /api/v1/events/multiple**
    - Description: Deletes multiple events by their IDs.
    - Query Parameters:
        - `eventIds`: The IDs of the events to delete.
    - Response: No content.

Please note that the request and response formats depend on the `Event` entity. The `Event` entity has the following fields: `eventId`, `name`, `creationTime`, `startTime`, `endTime`, `creatingUserId`, `location`, `venue`, `users`.


To execute this project, navigate to the project's directory and execute the following commands:

1. Build the Docker image:
```bash
docker build -t es-app -f Dockerfile .
```

2. Start the Docker containers:
```bash
docker compose up -d
```
