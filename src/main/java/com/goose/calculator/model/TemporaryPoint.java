package com.goose.calculator.model;

import lombok.*;

/**
 * {@code latitude} x;<p>
 * {@code longitude} y;<p>
 * {@code flightAltitude} z;<p>
 * {@code flightSpeed}: m/s;<p>
 * {@code course}: degree;
 */
@Data
@AllArgsConstructor
public class TemporaryPoint {
        private double latitude;
        private double longitude;
        private double flightAltitude;
        private double flightSpeed;
        private double course;
}
