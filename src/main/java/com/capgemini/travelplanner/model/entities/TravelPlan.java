package com.capgemini.travelplanner.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_plan;
    @NotNull
    private String travelName, destination;
    private String description;
    private LocalDate startDate = LocalDate.now().plusDays(1), endDate = LocalDate.now().plusDays(7);
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;

}
