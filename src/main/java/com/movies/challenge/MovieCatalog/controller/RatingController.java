package com.movies.challenge.MovieCatalog.controller;

import com.movies.challenge.MovieCatalog.model.Rating;
import com.movies.challenge.MovieCatalog.model.User;
import com.movies.challenge.MovieCatalog.repository.UserRepository;
import com.movies.challenge.MovieCatalog.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserRepository userRepository;

    // Endpoint para calificar una película (solo autenticados)
    @PostMapping("/{movieId}")
    public ResponseEntity<String> rateMovie(
            @PathVariable Integer movieId,
            @RequestParam Byte ratingValue,
            Authentication authentication) {
        Integer userId = getUserIdFromAuthentication(authentication);
        ratingService.rateMovie(movieId, userId, ratingValue);
        return ResponseEntity.ok("Movie rated successfully");
    }

    // Endpoint para eliminar la calificación de una película (solo autenticados)
    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> removeRating(
            @PathVariable Integer movieId,
            Authentication authentication) {
        Integer userId = getUserIdFromAuthentication(authentication);
        ratingService.removeRating(movieId, userId);
        return ResponseEntity.ok("Rating removed successfully");
    }

    // Endpoint para devolver todas las calificaciones del usuario autenticado
    @GetMapping
    public ResponseEntity<List<Rating>> getUserRatings(Authentication authentication) {
        Integer userId = getUserIdFromAuthentication(authentication);
        List<Rating> ratings = ratingService.getUserRatings(userId);
        return ResponseEntity.ok(ratings);
    }

    // Método auxiliar para obtener el ID del usuario desde el objeto Authentication
    private Integer getUserIdFromAuthentication(Authentication authentication) {
        // Obtener el nombre del usuario desde el Authentication principal
        String username = authentication.getName();
        System.out.println("Username extracted from token: " + username);
        // Buscar al usuario en la base de datos
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.getUserId();
    }
}

