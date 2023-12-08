package com.adiha.EventScheduler.utils.mapper;

import com.adiha.EventScheduler.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * EventMapper interface.
 * This interface is used to map between Event DTOs and Event entities.
 * It is implemented automatically by MapStruct.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

    /**
     * Updates an existing Event entity with values from an Event DTO.
     * The eventId field is ignored during the mapping process.
     *
     * @param dto    the Event DTO to map from
     * @param entity the Event entity to update
     */
    @Mapping(target = "eventId", ignore = true)
    void updateEventFromDto(Event dto, @MappingTarget Event entity);
}