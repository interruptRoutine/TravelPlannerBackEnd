package com.capgemini.travelplanner.model.Repositories;

import com.capgemini.travelplanner.model.entities.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SearchHistoryRepo extends JpaRepository<SearchHistory, UUID> {

}
