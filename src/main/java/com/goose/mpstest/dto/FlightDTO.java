package com.goose.mpstest.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"wayPointsNumber", "timeSpent"})
public class FlightDTO {
    private Long number;
    private int wayPointsNumber;
    private int timeSpent;

}
