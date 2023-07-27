package com.goose.mpstest.controller;

import com.goose.mpstest.dto.FlightDTO;
import com.goose.mpstest.model.Flight;
import com.goose.mpstest.model.WayPoint;
import com.goose.mpstest.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("airplanes/{id}/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public List<FlightDTO> getFlights(@PathVariable Long id) {
        return flightService.getFlights(id);
    }

    @GetMapping("/{flightId}")
    public Flight getFlight(@PathVariable Long id, @PathVariable Long flightId) {
        return flightService.getFlight(id, flightId);
    }

    @PostMapping("/calculate")
    public FlightDTO calculateFlight(@PathVariable Long id, @RequestBody List<WayPoint> wayPoints) throws Exception {
        return flightService.calculateFlight(id, wayPoints);
    }

    @DeleteMapping("/{flightId}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id, @PathVariable Long flightId) {
        flightService.deleteFlight(id, flightId);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("ok");
    }
}
