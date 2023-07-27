package com.goose.calculator.controller;

import com.goose.calculator.dto.AirplaneDTO;
import com.goose.calculator.service.AirplaneService;
import com.goose.calculator.model.Airplane;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("airplanes")
@RequiredArgsConstructor
public class AirplaneController {

    private final AirplaneService airplaneService;

    @GetMapping
    public List<AirplaneDTO> getAirplanes() {
        return airplaneService.getAirplanes();
    }

    @GetMapping("/{id}")
    public Airplane getAirplane(@PathVariable Long id) {
        return airplaneService.getAirplane(id);
    }

    @PostMapping
    public AirplaneDTO addAirplane(@RequestBody Airplane airplane) {
        return airplaneService.addAirplane(airplane);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAirplane(@PathVariable Long id) {
        airplaneService.deleteAirplane(id);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("ok");
    }

}
