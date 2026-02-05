package com.capgemini.travelplanner.model.controllers;

import com.capgemini.travelplanner.model.dtos.PlanDto;
import com.capgemini.travelplanner.model.services.TravelPlanService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/travels")
public class TravelController {
    private final TravelPlanService travelPlanService;
    @Value("${geoapify.api_key}")
    private String geoapifyApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public TravelController(TravelPlanService travelPlanService) {
        this.travelPlanService = travelPlanService;
    }

    @GetMapping("/")
    public String home() {
        return "Ao io sto a funziona";
    }

    @GetMapping("/city")
    public String getCity(Authentication authentication, @RequestParam("city") String city){
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.geoapify.com")
                .path("/v1/geocode/search")
                .queryParam("text", city)
                .queryParam("format", "json")
                .queryParam("apiKey", geoapifyApiKey)
                .build()
                .toUriString();

        travelPlanService.saveSearchHistory(authentication, city);

        return restTemplate.getForObject(url, String.class);
    }


    @GetMapping("/coordinates")
    public String getCoordinates(@RequestParam("city") String city) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.geoapify.com")
                .path("/v1/geocode/search")
                .queryParam("text", city)
                .queryParam("format", "json")
                .queryParam("apiKey", geoapifyApiKey)
                .build()
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        // Estrai solo le coordinate (latitudine e longitudine) dal JSON di risposta
        try {
            com.fasterxml.jackson.databind.JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
            if (root.has("results") && root.get("results").isArray() && !root.get("results").isEmpty()) {
                com.fasterxml.jackson.databind.JsonNode firstResult = root.get("results").get(0);
                if (firstResult.has("lat") && firstResult.has("lon")) {
                    double lat = firstResult.get("lat").asDouble();
                    double lon = firstResult.get("lon").asDouble();
                    return lat + "," + lon;
                }
            }
            return "Coordinate non trovate";
        } catch (Exception e) {
            return "Errore nell'elaborazione della risposta: " + e.getMessage();
        }
    }

    @GetMapping("/travelplan")
    public String getRoute(@RequestParam("start") String start, @RequestParam("end") String end) {
        String startCoordinates = getCoordinates(start);
        String endCoordinates = getCoordinates(end);
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.geoapify.com")
                .path("/v1/routing")
                .queryParam("waypoints", startCoordinates + "|" + endCoordinates)
                .queryParam("mode", "drive")
                .queryParam("lang", "it")
                .queryParam("apiKey", geoapifyApiKey)
                .build()
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    @PostMapping("/travelplan")
    public ResponseEntity<String> setTravelPlan(Authentication authentication, @RequestBody PlanDto planDto) {
        try {
            travelPlanService.saveTravelPlan(authentication, planDto);
            return ResponseEntity.ok("Travel plan salvato");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore nel salvataggio: " + e.getMessage());
        }
    }

}
