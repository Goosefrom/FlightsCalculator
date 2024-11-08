package com.goose.calculator.model;

import lombok.*;

/**
 * {@code maxSpeed}: m/s;<p>
 * {@code maxAcceleration}: m/s^2;<p>
 * {@code rateAltitudeChange}: m/s;<p>
 * {@code rateCourseChange}: degree/s;
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneCharacteristics {
        private double maxSpeed;
        private double maxAcceleration;
        private double rateAltitudeChange;
        private double rateCourseChange;
}
