package com.capgemini.travelplanner.model.dtos;

import com.capgemini.travelplanner.model.enums.DistanceUnit;
import com.capgemini.travelplanner.model.enums.TemperatureUnit;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserModifyDto {
    private String email, name, password /*TODO*/, location;
    private LocalDate dateOfBirth;
    private TemperatureUnit temperatureUnit;
    private DistanceUnit distanceUnit;
}
