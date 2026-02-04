package com.capgemini.travelplanner.model.Repositories;

import com.capgemini.travelplanner.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}
