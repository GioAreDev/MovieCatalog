package com.movies.challenge.MovieCatalog.controller;

import com.movies.challenge.MovieCatalog.exceptions.BadRequestException;
import com.movies.challenge.MovieCatalog.model.Rating;
import com.movies.challenge.MovieCatalog.model.User;
import com.movies.challenge.MovieCatalog.repository.UserRepository;
import com.movies.challenge.MovieCatalog.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{movieId}")
    public ResponseEntity<?> rateMovie(
            @PathVariable Integer movieId,
            @RequestParam Byte ratingValue,
            Authentication authentication) {
        try {
            Integer userId = getUserIdFromAuthentication(authentication);
            ratingService.rateMovie(movieId, userId, ratingValue);
            return ResponseEntity.ok("Movie rated successfully");
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> removeRating(
            @PathVariable Integer movieId,
            Authentication authentication) {
       try{
           Integer userId = getUserIdFromAuthentication(authentication);
           ratingService.
                   removeRating(movieId, userId);
           return ResponseEntity.ok("Rating removed successfully");
       }catch (BadRequestException ex){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
       }
    }


    @GetMapping
    public ResponseEntity<List<Rating>> getUserRatings(Authentication authentication) {
        Integer userId = getUserIdFromAuthentication(authentication);
        List<Rating> ratings = ratingService.getUserRatings(userId);
        return ResponseEntity.ok(ratings);
    }


    private Integer getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Username extracted from token: " + username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.getUserId();
    }
}

