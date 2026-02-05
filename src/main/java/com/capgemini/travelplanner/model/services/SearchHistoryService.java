package com.capgemini.travelplanner.model.services;

import com.capgemini.travelplanner.model.Repositories.SearchHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchHistoryService {
    @Autowired
    SearchHistoryRepo searchHistoryRepo;

}
