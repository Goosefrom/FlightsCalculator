package com.goose.calculator.dto;

import com.goose.calculator.model.AirplaneCharacteristics;
import com.goose.calculator.model.TemporaryPoint;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"airplaneCharacteristics", "position", "flightsNumber"})
public class AirplaneDTO {
    private Long id;
    private AirplaneCharacteristics airplaneCharacteristics;
    private TemporaryPoint position;
    private int flightsNumber;

}
