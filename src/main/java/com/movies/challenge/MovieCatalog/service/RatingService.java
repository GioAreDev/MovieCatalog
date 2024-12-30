package com.movies.challenge.MovieCatalog.service;

import com.movies.challenge.MovieCatalog.model.Movie;
import com.movies.challenge.MovieCatalog.model.Rating;
import com.movies.challenge.MovieCatalog.model.User;
import com.movies.challenge.MovieCatalog.repository.IMovieRepository;
import com.movies.challenge.MovieCatalog.repository.IRatingRepository;
import com.movies.challenge.MovieCatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private IRatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IMovieRepository movieRepository;
    public void rateMovie(Integer movieId, Integer userId, Byte ratingValue) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (ratingRepository.existsByMovieAndUser(movie, user)) {
            throw new IllegalArgumentException("You have already rated this movie");
        }

        Rating rating = new Rating();
        rating.setMovie(movie);
        rating.setUser(user);
        rating.setRating(ratingValue);
        rating.setCreatedDate(LocalDateTime.now());

        ratingRepository.save(rating);
    }


    /**
     * Elimina la calificación de una película por parte del usuario autenticado.
     */
    public void removeRating(Integer movieId, Integer userId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Rating rating = ratingRepository.findByMovieAndUser(movie, user)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found for this movie"));

        ratingRepository.delete(rating);
    }

    /**
     * Devuelve todas las calificaciones hechas por un usuario.
     */
    public List<Rating> getUserRatings(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ratingRepository.findAllByUserId(userId);
    }
}

