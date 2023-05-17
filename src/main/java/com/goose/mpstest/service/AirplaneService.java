package com.goose.mpstest.service;

import com.goose.mpstest.dto.AirplaneDTO;
import com.goose.mpstest.model.Airplane;
import com.goose.mpstest.model.Flight;
import com.goose.mpstest.model.WayPoint;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AirplaneService {

    List<AirplaneDTO> getAirplanes();

    Airplane getAirplane(Long id);

    AirplaneDTO addAirplane(Airplane airplane);

    AirplaneDTO updateAirplane(Long id, Airplane airplane);

    void deleteAirplane(Long id);
}
