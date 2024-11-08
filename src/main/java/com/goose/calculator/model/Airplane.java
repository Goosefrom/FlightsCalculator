package com.goose.calculator.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Airplane {
        @Id
        private Long id;
        private AirplaneCharacteristics airplaneCharacteristics;
        private TemporaryPoint position;
        private List<Flight> flights;

        public Airplane(AirplaneCharacteristics airplaneCharacteristics,
                        TemporaryPoint position,
                        List<Flight> flights) {
                this.airplaneCharacteristics = airplaneCharacteristics;
                this.position = position;
                this.flights = flights;
        }
}
