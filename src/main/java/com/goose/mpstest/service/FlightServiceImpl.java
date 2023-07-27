package com.goose.mpstest.service;

import com.goose.mpstest.config.PlaneCalculation;
import com.goose.mpstest.config.mappers.FlightMapper;
import com.goose.mpstest.dto.FlightDTO;
import com.goose.mpstest.exception.AirplaneException;
import com.goose.mpstest.exception.ErrorType;
import com.goose.mpstest.model.Airplane;
import com.goose.mpstest.model.Flight;
import com.goose.mpstest.model.TemporaryPoint;
import com.goose.mpstest.model.WayPoint;
import com.goose.mpstest.repository.AirplaneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService{

    private final AirplaneRepository airplaneRepository;

    private final PlaneCalculation planeCalculation;

    private final FlightMapper mapper;

    @Override
    public List<FlightDTO> getFlights(Long id) {
        List<FlightDTO> flights = airplaneRepository.findById(id).orElseThrow().getFlights().stream().map(mapper::toDTO).toList();
        log.info("Get flight list for airplane id={}", id);
        return flights;
    }

    @Override
    public Flight getFlight(Long id, Long number) {
        Flight flight = airplaneRepository.findById(id)
                .orElseThrow(() -> new AirplaneException(ErrorType.NOT_FOUND, "airplane with id not found"))
                .getFlights().get(number.intValue());
        log.info("Get flight N:{} for airplane id={}", number, id);
        return flight;
    }

    @Override
    public FlightDTO calculateFlight(Long id, List<WayPoint> wayPoints) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new AirplaneException(ErrorType.NOT_FOUND, "airplane not found"));

        List<TemporaryPoint> temporaryPoints = planeCalculation
                .calculateRoute(airplane, wayPoints);

        Flight lastFlight = new Flight((long) airplane.getFlights().size(), wayPoints, temporaryPoints);
        List<Flight> flights = airplane.getFlights();
        flights.add(lastFlight);
        airplane.setPosition(temporaryPoints.get(temporaryPoints.size() - 1));
        airplane.setFlights(flights);
        airplaneRepository.save(airplane);
        log.info("Airplane (id={}) updated with new flight", id);
        return mapper.toDTO(lastFlight);
    }

    @Override
    public void deleteFlight(Long id, Long number) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new AirplaneException(ErrorType.NOT_FOUND, "airplane with id not found"));

        airplane.getFlights().remove(number.intValue());
        airplaneRepository.save(airplane);
        log.info("Deleted flight N:{} from airplane id={}", number, id);
    }

}
