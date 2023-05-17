package com.goose.mpstest.config.mappers;

import com.goose.mpstest.dto.AirplaneDTO;
import com.goose.mpstest.model.Airplane;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AirplaneMapper {

    @Mapping(target = "flightsNumber", expression = "java(airplane.getFlights().size())")
    public abstract AirplaneDTO toDTO(Airplane airplane);
}
