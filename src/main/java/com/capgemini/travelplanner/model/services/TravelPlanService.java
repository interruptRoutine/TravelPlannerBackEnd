package com.capgemini.travelplanner.model.services;

import com.capgemini.travelplanner.model.Repositories.SearchHistoryRepo;
import com.capgemini.travelplanner.model.Repositories.TravelPlanRepo;
import com.capgemini.travelplanner.model.Repositories.UserRepo;
import com.capgemini.travelplanner.model.dtos.PlanDto;
import com.capgemini.travelplanner.model.entities.SearchHistory;
import com.capgemini.travelplanner.model.entities.TravelPlan;
import com.capgemini.travelplanner.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TravelPlanService {
    @Autowired
    TravelPlanRepo travelPlanRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    private SearchHistoryRepo searchHistoryRepo;

    public void saveSearchHistory(Authentication authentication, String city) {
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setCity(city);
        searchHistory.setSearchTime(LocalDateTime.now());
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Utente non trovato: " + email));
        searchHistory.setUser(user);
        user.getSearchHistory().add(searchHistory);
        searchHistoryRepo.save(searchHistory);
    }

    public TravelPlan saveTravelPlan(Authentication authentication, PlanDto planSavingDto) {
        TravelPlan travelPlan = new TravelPlan();
        travelPlan.setTravelName(planSavingDto.getTravelName());
        travelPlan.setStartCity(planSavingDto.getStartCity());
        travelPlan.setEndCity(planSavingDto.getEndCity());
        travelPlan.setStartDate(planSavingDto.getStartDate());
        travelPlan.setCreationTime(LocalDateTime.now());

        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Utente non trovato: " + email));
        travelPlan.setUser(user);
        user.getTravelPlanSaved().add(travelPlan);
        return travelPlanRepo.save(travelPlan);
    }

    public Optional<TravelPlan> findTravelPlanByName(Authentication authentication, String travelName) {
        String email = authentication.getName();
        return travelPlanRepo.findByTravelNameAndUserEmail(travelName, email);
    }

    public void deleteTravelPlanByName(Authentication authentication, String travelName) {
        String email = authentication.getName();
        travelPlanRepo.deleteByTravelNameAndUserEmail(travelName, email);
    }

    public void deleteAllTravelPlansForUser(Authentication authentication) {
        String email = authentication.getName();
        travelPlanRepo.deleteAllByUserEmail(email);
    }
}
