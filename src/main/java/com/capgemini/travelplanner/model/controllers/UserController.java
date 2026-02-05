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
        String loggedInToken = null;

        String tokenUtente = userService.loginUser(userDto);

        Cookie cookie = new Cookie("token", tokenUtente);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("token", tokenUtente));
    }

    /**
     * Elimina l'utente autenticato
     * @param authentication
     * @return Token Utente
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication.getName());
        return ResponseEntity.ok().build();
    }


    /**
     * Modifica i dati dell'utente autenticato
     * @param authentication
     * @param userDto
     * @return Restituisce User Modificato
     */
    @PutMapping("/modify")
    public ResponseEntity<User> modifyUser(Authentication authentication, @RequestBody UserModifyDto userDto) {
        return ResponseEntity.ok(userService.modifyUser(authentication.getName(), userDto));
    }
}
