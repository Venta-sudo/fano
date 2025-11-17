package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Méthode pour trouver un utilisateur par son nom d'utilisateur (username)
    Optional<User> findByUsername(String username);

    // Méthode pour trouver un utilisateur par son adresse e-mail (email)
    Optional<User> findByEmail(String email);

    // Méthode pour vérifier si un nom d'utilisateur existe déjà
    Boolean existsByUsername(String username);

    // Méthode pour vérifier si une adresse e-mail existe déjà
    Boolean existsByEmail(String email);
}