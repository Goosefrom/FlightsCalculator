package com.goose.mpstest.dto;

import com.goose.mpstest.model.AirplaneCharacteristics;
import com.goose.mpstest.model.Flight;
import com.goose.mpstest.model.TemporaryPoint;
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
