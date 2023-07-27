package com.goose.mpstest.service;

import com.goose.mpstest.dto.FlightDTO;
import com.goose.mpstest.model.Flight;
import com.goose.mpstest.model.WayPoint;

import java.util.List;

public interface FlightService {

    List<FlightDTO> getFlights(Long id);

    Flight getFlight(Long id, Long number);

    FlightDTO calculateFlight(Long id, List<WayPoint> wayPoints) throws Exception;

    void deleteFlight(Long id, Long number);
}
