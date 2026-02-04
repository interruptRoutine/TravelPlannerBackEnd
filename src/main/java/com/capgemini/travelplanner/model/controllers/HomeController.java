package com.capgemini.travelplanner.model.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping("/")
    public String home() {
        return  "|--------------------------------------------------------------------|\n" +
                "| Questo è un progetto di fine academy di Capgemini: Travel Planner. |\n" +
                "|                  Per le funzioni base consultare:                  |\n" +
                "|                localhost:8080/swagger-ui/index.html                |\n" +
                "|--------------------------------------------------------------------|";
    }
}
