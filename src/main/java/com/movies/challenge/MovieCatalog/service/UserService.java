package com.movies.challenge.MovieCatalog.service;

import com.movies.challenge.MovieCatalog.model.User;
import com.movies.challenge.MovieCatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User register(User user) {
        // Encriptar contraseña antes de guardar
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setCreatedDate(LocalDateTime.now());
        return userRepository.save(user);
    }
}


