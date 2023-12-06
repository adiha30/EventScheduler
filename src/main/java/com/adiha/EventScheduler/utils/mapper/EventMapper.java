package com.adiha.EventScheduler.utils.mapper;

import com.adiha.EventScheduler.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    void updateEventFromDto(Event dto, @MappingTarget Event entity);
}