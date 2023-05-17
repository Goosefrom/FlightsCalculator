package com.goose.mpstest.model;

import lombok.*;

/**
 * {@code latitude} x;<p>
 * {@code longitude} y;<p>
 * {@code flightAltitude} z;<p>
 * {@code flightSpeed}: m/s;<p>
 * {@code course}: degree;
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryPoint {
        private double latitude;
        private double longitude;
        private double flightAltitude;
        private double flightSpeed;
        private double course;
}
