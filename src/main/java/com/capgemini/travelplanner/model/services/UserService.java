package com.capgemini.travelplanner.model.services;

import com.capgemini.travelplanner.model.Repositories.UserRepo;
import com.capgemini.travelplanner.model.dtos.UserLoginDto;
import com.capgemini.travelplanner.model.dtos.UserModifyDto;
import com.capgemini.travelplanner.model.dtos.UserRegisterDto;
import com.capgemini.travelplanner.model.entities.User;
import com.capgemini.travelplanner.model.enums.DistanceUnit;
import com.capgemini.travelplanner.model.enums.Roles;
import com.capgemini.travelplanner.model.enums.TemperatureUnit;
import com.capgemini.travelplanner.model.exceptions.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    @Lazy
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        User user = userRepo.findByToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with token: " + token));

        return new org.springframework.security.core.userdetails.User(user.getToken(), user.getPassword(), new ArrayList<>());
    }

    /**
     * Registrazione di un nuovo utente
     * @param userDto Dati dell'utente da registrare
     * @return Utente registrato
     */
    public User registerUser(UserRegisterDto userDto) {
        User user = new User(); //Nuovo utente da salvare

        //Check existing Users
        if (userRepo.existsByEmail(userDto.getEmail())) {
            throw new InvalidCredentialsException("Email già in uso. Fai il login o usa un'altra email.");
        }

        if (userDto.getPassword() == null || !userDto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&-])[A-Za-z\\d@$!%*?&-]{8,}$")) {
            throw new InvalidCredentialsException("Password non valida. La password deve contenere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale.");
        } else
            user.setPassword(encoder.encode(userDto.getPassword()));

        user.setEmail(userDto.getEmail());
        user.setDob(userDto.getDob());
        user.setName(userDto.getName());
        user.setLocation(userDto.getLocation());

        //Default settings
        String newToken;
        do {
            newToken = UUID.randomUUID().toString();
        } while (userRepo.findByToken(newToken).isPresent());

        user.setToken(newToken);
        user.setRole(Roles.USER);
        user.setDistanceUnit(DistanceUnit.KILOMETERS);
        user.setTemperatureUnit(TemperatureUnit.CELSIUS);

        //Salvataggio utente
        return userRepo.save(user);
    }

    public String loginUser(UserLoginDto userDto) {
        Optional<User> op = userRepo.findByEmail(userDto.getEmail());
        if (op.isEmpty())
            throw new InvalidCredentialsException("Utente non trovato.");
        if(!encoder.matches(userDto.getPassword(), op.get().getPassword()))
            throw new InvalidCredentialsException("Password inserita non valida.");

        return op.get().getToken();
    }

    public void deleteUser(String token) {
        Optional<User> u;
        String email = "";
        try {
            u = userRepo.findByToken(token);
            email = u.get().getEmail();
        } catch (Exception e) {
            throw new InvalidCredentialsException("Token non valido.");
        }
        userRepo.deleteByEmail(email);
    }

    public User modifyUser(String token, UserModifyDto userDto) {
        User user = userRepo.findByToken(token).orElseThrow(() -> new InvalidCredentialsException("Token non valido."));

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank() && !userDto.getEmail().equals(user.getEmail())) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isBlank() && !userDto.getName().equals(user.getName())) {
            user.setName(userDto.getName());
        }
        if (userDto.getLocation() != null && !userDto.getLocation().isBlank() && !userDto.getLocation().equals(user.getLocation())) {
            user.setLocation(userDto.getLocation());
        }
        if (userDto.getDateOfBirth() != null && !userDto.getDateOfBirth().equals(user.getDob())) {
            user.setDob(userDto.getDateOfBirth());
        }
        if (userDto.getTemperatureUnit() != null && !userDto.getTemperatureUnit().equals(user.getTemperatureUnit())) {
            user.setTemperatureUnit(userDto.getTemperatureUnit());
        }
        if (userDto.getDistanceUnit() != null && !userDto.getDistanceUnit().equals(user.getDistanceUnit())) {
            user.setDistanceUnit(userDto.getDistanceUnit());
        }
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank() && !encoder.matches(userDto.getPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(userDto.getPassword()));
        }

        return userRepo.save(user);
    }


}
