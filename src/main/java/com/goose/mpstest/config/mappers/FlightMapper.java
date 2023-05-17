package com.goose.mpstest.config.mappers;

import com.goose.mpstest.dto.FlightDTO;
import com.goose.mpstest.model.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class FlightMapper {

    @Mapping(target = "wayPointsNumber", expression = "java(flight.getWayPoints().size())")
    @Mapping(target = "timeSpent", expression = "java(flight.getPassedPoints().size())")
    public abstract FlightDTO toDTO(Flight flight);
}
