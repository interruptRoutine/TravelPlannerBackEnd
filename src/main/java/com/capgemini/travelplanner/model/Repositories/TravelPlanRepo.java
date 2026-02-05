package com.capgemini.travelplanner.model.Repositories;

import com.capgemini.travelplanner.model.entities.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TravelPlanRepo extends JpaRepository<TravelPlan, UUID> {
    @Override
    Optional<TravelPlan> findById(UUID id);

    Optional<TravelPlan> findByTravelNameAndUserEmail(String travelName, String email);

    void deleteByTravelNameAndUserEmail(String travelName, String email);

    void deleteAllByUserEmail(String email);
}
