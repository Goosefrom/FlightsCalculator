package com.goose.calculator.service;

import com.goose.calculator.dto.AirplaneDTO;
import com.goose.calculator.model.Airplane;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AirplaneService {

    List<AirplaneDTO> getAirplanes();

    Airplane getAirplane(Long id);

    AirplaneDTO addAirplane(Airplane airplane);

    void deleteAirplane(Long id);
}
