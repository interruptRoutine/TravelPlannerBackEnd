package com.capgemini.travelplanner.model.controllers;

import com.capgemini.travelplanner.model.dtos.UserLoginDto;
import com.capgemini.travelplanner.model.dtos.UserModifyDto;
import com.capgemini.travelplanner.model.dtos.UserRegisterDto;
import com.capgemini.travelplanner.model.entities.User;
import com.capgemini.travelplanner.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        // Restituisce le informazioni dell'utente autenticato
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Utente non autenticato. Effettua il login.");
        }
        String email = (String) authentication.getPrincipal();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Registra un nuovo utente
     * @param userDto
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDto userDto) {
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    /**
     * Autentica un utente già registrato
     * @param userDto
     * @return Token Utente
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserLoginDto userDto, HttpServletResponse response) {
        String tokenUtente = userService.loginUser(userDto);

        Cookie cookie = new Cookie("token", tokenUtente);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("token", tokenUtente));
    }

    /**
     * Elimina l'utente autenticato
     * @param authentication Oggetto di autenticazione Spring Security
     * @return Risposta vuota con status 200
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }


    /**
     * Modifica i dati dell'utente autenticato
     * @param authentication Oggetto di autenticazione Spring Security
     * @param userDto DTO con i dati da modificare
     * @return Restituisce User Modificato
     */
    @PutMapping("/modify")
    public ResponseEntity<User> modifyUser(Authentication authentication, @RequestBody UserModifyDto userDto) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(userService.modifyUserByEmail(email, userDto));
    }
}
