package com.capgemini.travelplanner.model.dtos;

import com.capgemini.travelplanner.model.enums.Roles;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegisterDto {
    private String email, password, name, location;
    private LocalDate dob;

}
