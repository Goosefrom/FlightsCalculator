package com.goose.calculator.config.mappers;

import com.goose.calculator.dto.AirplaneDTO;
import com.goose.calculator.model.Airplane;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AirplaneMapper {

    @Mapping(target = "flightsNumber", expression = "java(airplane.getFlights().size())")
    public abstract AirplaneDTO toDTO(Airplane airplane);
}
