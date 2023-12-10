# EventScheduler API Documentation

## Authentication and Authorization

All CRUD endpoints require a registered user with an authentication token. The update and delete operations can only be performed by the user who created the event. The only non-authenticated endpoints are the register and authenticate ones.

### Security Considerations

Ensure the secure handling of authentication tokens and be aware of potential security risks. Protect the token and implement secure practices to safeguard user data.

## Endpoints

### Event Endpoints

Each of the following endpoints requires an authenticated user. The user must include their authentication token in the header of the request. Update and delete operations can only be performed by the user who created the event.

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

### Authentication Endpoints

The following endpoints do not require authentication:

1. **POST /api/v1/auth/register**
    - Description: Registers a new user.
    - Request Body: The user's registration details.
    - Response: The authentication token.

2. **POST /api/v1/auth/authenticate**
    - Description: Authenticates a user.
    - Request Body: The user's login details.
    - Response: The authentication token.

## Request and Response Format

Note that the request and response formats depend on the `Event` and `User` entities. The `Event` entity has the following fields: `eventId`, `name`, `creationTime`, `startTime`, `endTime`, `creatingUserId`, `location`, `venue`, `users`. The `User` entity has the following fields: `userId`, `username`, `password`, `role`.

## Execution

To execute this project, navigate to the project's directory and execute the following commands:

```bash
#Be sure to login to docker
docker login

# Run gradle wrapper
gradle wrapper

# Build the Docker image
docker build -t es-app -f Dockerfile .

# Start the Docker containers
docker-compose up -d
