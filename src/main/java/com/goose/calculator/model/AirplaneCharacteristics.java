package com.goose.calculator.model;

import lombok.*;

/**
 * {@code maxSpeed}: m/s;<p>
 * {@code maxAcceleration}: m/s^2;<p>
 * {@code rateAltitudeChange}: m/s;<p>
 * {@code rateCourseChange}: degree/s;
 */

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AirplaneCharacteristics {
        private double maxSpeed;
        private double maxAcceleration;
        private double rateAltitudeChange;
        private double rateCourseChange;
}
