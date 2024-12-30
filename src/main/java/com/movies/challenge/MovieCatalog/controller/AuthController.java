package com.movies.challenge.MovieCatalog.controller;
import com.movies.challenge.MovieCatalog.dto.AuthRequest;
import com.movies.challenge.MovieCatalog.dto.AuthResponse;
import com.movies.challenge.MovieCatalog.dto.UserRegisterRequest;
import com.movies.challenge.MovieCatalog.dto.UserResponse;
import com.movies.challenge.MovieCatalog.model.User;
import com.movies.challenge.MovieCatalog.security.JwtUtil;
import com.movies.challenge.MovieCatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint para autenticar usuarios y generar token JWT
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest request) {
        // Buscar usuario por email
        Optional<User> userOptional = userService.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        User user = userOptional.get();

        // Verificar la contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Generar token JWT
        String token = jwtUtil.generateToken(user);

        // Responder con el token
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // Endpoint para registrar nuevos usuarios
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        // Verificar si ya existe un usuario con el mismo email
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use");
        }

        // Crear y registrar el usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setRole("ADMIN".equals(request.getRole())? User.Role.ADMIN:User.Role.USER);
        User registeredUser = userService.register(user);

        // Responder con la información del usuario registrado
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(
                registeredUser.getUserId(),
                registeredUser.getUsername(),
                registeredUser.getEmail(),
                registeredUser.getRole().name()
        ));
    }
}
