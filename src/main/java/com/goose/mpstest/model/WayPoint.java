package com.goose.mpstest.model;

import lombok.*;

/**
 * {@code latitude} x;<p>
 * {@code longitude} y;<p>
 * {@code flightAltitude} z;<p>
 * {@code flightSpeed}: m/s;
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class WayPoint {
        private double latitude;
        private double longitude;
        private double flightAltitude;
        private double flightSpeed;
}
