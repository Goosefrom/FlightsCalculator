package com.goose.mpstest.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
        private Long number;
        private List<WayPoint> wayPoints;
        private List<TemporaryPoint> passedPoints;
}
