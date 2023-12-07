package com.adiha.EventScheduler.utils.mapper;

import com.adiha.EventScheduler.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "eventId", ignore = true)
    void updateEventFromDto(Event dto, @MappingTarget Event entity);
}