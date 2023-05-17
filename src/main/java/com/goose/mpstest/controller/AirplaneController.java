package com.goose.mpstest.controller;

import com.goose.mpstest.dto.AirplaneDTO;
import com.goose.mpstest.model.Airplane;
import com.goose.mpstest.model.Flight;
import com.goose.mpstest.model.WayPoint;
import com.goose.mpstest.service.AirplaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/{id}/update")
    public AirplaneDTO updateAirplane(@PathVariable Long id, @RequestBody Airplane airplane) {
        return airplaneService.updateAirplane(id, airplane);
    }



}
