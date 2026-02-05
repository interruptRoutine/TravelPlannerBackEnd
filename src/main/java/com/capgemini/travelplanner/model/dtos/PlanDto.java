package com.capgemini.travelplanner.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDto {
    private String travelName;
    private String startCity, endCity;
    private LocalDate startDate;
}
