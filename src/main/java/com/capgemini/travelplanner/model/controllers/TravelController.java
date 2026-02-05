package com.capgemini.travelplanner.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/travels")
public class TravelController {
    @Value("${geoapify.api_key}")
    private String geoapifyApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String home() {
        return "Ao io sto a funziona";
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
        //https://api.geoapify.com/v1/routing?waypoints=42.0517987,12.6183967|41.85949955,12.58611768937552&mode=drive&lang=it&apiKey=YOUR_API_KEY
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

}
