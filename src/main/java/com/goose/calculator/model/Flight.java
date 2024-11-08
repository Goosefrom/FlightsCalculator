package com.goose.calculator.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@AllArgsConstructor
public class Flight {
        @Id
        private Long number;
        private List<WayPoint> wayPoints;
        private List<TemporaryPoint> passedPoints;
}
