package com.capgemini.travelplanner.model.entities;

import com.capgemini.travelplanner.model.enums.DistanceUnit;
import com.capgemini.travelplanner.model.enums.Roles;
import com.capgemini.travelplanner.model.enums.TemperatureUnit;
import com.capgemini.travelplanner.model.exceptions.InvalidCredentialsException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Email(regexp = ".+@.+\\..+", message = "Formato email non valido.")
    @NotNull
    private String email;
    @NotNull
    @NotBlank(message = "Il nome non può essere vuoto.")
    private String name;
    @NotNull
    @NotBlank(message = "La località non può essere vuota.")
    private String location;
    @NotNull
    private String password;
    @NotNull
    private LocalDate dob;

    //TODO: da implementare per autenticazione
    private String token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TravelPlan> travelPlan = new HashSet<>();

    //ENUMS
    private Roles role;
    private TemperatureUnit temperatureUnit;
    private DistanceUnit distanceUnit;

    //Getter e Setter Personalizzati:

    public void setEmail(String email) {
        if (email == null || !email.matches(".+@.+\\..+")) {
            throw new IllegalArgumentException("Formato email non valido.");
        }
        this.email = email;
    }

    public void setDob(LocalDate dob) {
        if (dob == null || dob.isAfter(LocalDate.now().minusYears(16))) {
            throw new IllegalArgumentException("L'utente deve avere almeno 16 anni.");
        }
        this.dob = dob;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto.");
        }
        this.name = name;
    }

    public void setLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("La località non può essere vuota.");
        }
        this.location = location;
    }
}
