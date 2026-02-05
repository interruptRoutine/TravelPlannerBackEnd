package com.capgemini.travelplanner.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPlan;
    @NotNull
    @UniqueElements
    private String travelName;
    private String startCity, endCity;
    private LocalDate startDate;
    private LocalDateTime creationTime;
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "email")
    private User user;
}
