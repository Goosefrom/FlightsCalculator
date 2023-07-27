package com.goose.calculator.service;

import com.goose.calculator.dto.FlightDTO;
import com.goose.calculator.model.Flight;
import com.goose.calculator.model.WayPoint;

import java.util.List;

public interface FlightService {

    List<FlightDTO> getFlights(Long id);

    Flight getFlight(Long id, Long number);

    FlightDTO calculateFlight(Long id, List<WayPoint> wayPoints) throws Exception;

    void deleteFlight(Long id, Long number);
}
