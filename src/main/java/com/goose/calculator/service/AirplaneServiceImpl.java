package com.goose.calculator.service;

import com.goose.calculator.dto.AirplaneDTO;
import com.goose.calculator.exception.AirplaneException;
import com.goose.calculator.exception.ErrorType;
import com.goose.calculator.config.mappers.AirplaneMapper;
import com.goose.calculator.model.Airplane;
import com.goose.calculator.repository.AirplaneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {

    private final AirplaneRepository repository;
    private final AirplaneMapper mapper;

    @Override
    public List<AirplaneDTO> getAirplanes() {
        List<AirplaneDTO> airplanes = repository.findAll().stream().map(mapper::toDTO).toList();
        log.info("Airplanes to return: {}", airplanes);

        return airplanes;
    }

    @Override
    public Airplane getAirplane(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AirplaneException(ErrorType.NOT_FOUND, "airplane not found"));
    }


    @Override
    public AirplaneDTO addAirplane(Airplane airplane) {
        if (repository.existsById(airplane.getId()))
            throw new AirplaneException(ErrorType.ALREADY_OCCUPIED, "airplane with this id already exist");
        AirplaneDTO dto = mapper.toDTO(repository.save(airplane));
        log.info("Airplane saved id={}", dto.getId());
        return dto;
    }

    @Override
    public void deleteAirplane(Long id) {
        if (!repository.existsById(id))
            throw new AirplaneException(ErrorType.ALREADY_OCCUPIED, "airplane with this id not found");
        repository.deleteById(id);
    }
}
