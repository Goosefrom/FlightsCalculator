package com.goose.calculator;

import com.goose.calculator.model.Airplane;
import com.goose.calculator.model.TemporaryPoint;
import com.goose.calculator.model.AirplaneCharacteristics;
import com.goose.calculator.repository.AirplaneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
public class MpsTestApplication implements CommandLineRunner {

    @Autowired
    AirplaneRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(MpsTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.findAll().isEmpty()) {
            Airplane airplane1 = new Airplane(1L,
                    new AirplaneCharacteristics(3.0,
                            0.1,
                            0.1,
                            90),
                    new TemporaryPoint(0,0,0,0,0),
                    List.of());
            Airplane airplane2 = new Airplane(2L,
                    new AirplaneCharacteristics(6.0,
                            1.1,
                            0.5,
                            30),
                    new TemporaryPoint(1.0,1.,1.,0.,180.),
                    List.of());
            Airplane airplane3 = new Airplane(3L,
                    new AirplaneCharacteristics(100.0,
                            7.5,
                            0.7,
                            60),
                    new TemporaryPoint(0.0,3.,0.,2.,270.),
                    List.of());

            List<Airplane> results = repository.saveAll(List.of(airplane1, airplane2, airplane3));
            log.info("Airplanes are saved: {}", results);

        }
    }
}