package com.movies.challenge.MovieCatalog.service;

import com.movies.challenge.MovieCatalog.exceptions.BadRequestException;
import com.movies.challenge.MovieCatalog.model.Movie;
import com.movies.challenge.MovieCatalog.model.Rating;
import com.movies.challenge.MovieCatalog.model.User;
import com.movies.challenge.MovieCatalog.repository.IMovieRepository;
import com.movies.challenge.MovieCatalog.repository.IRatingRepository;
import com.movies.challenge.MovieCatalog.repository.UserRepository;
import com.movies.challenge.MovieCatalog.service.interfaces.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingService implements IRatingService<Rating,Integer> {

    @Autowired
    private IRatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IMovieRepository movieRepository;
    @Override
    @CacheEvict(value = "ratings", key = "#userId")
    public void rateMovie(Integer movieId, Integer userId, Byte ratingValue) {
        try{
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new BadRequestException("Movie not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (ratingRepository.existsByMovieAndUser(movie, user)) {
            throw new BadRequestException("You have already rated this movie");
        }

        if(ratingValue == null || ratingValue <= 0){
            throw new BadRequestException("Rating value is required");
        }

            Rating rating = new Rating();
            rating.setMovie(movie);
            rating.setUser(user);
            rating.setRating(ratingValue);
            rating.setCreatedDate(LocalDateTime.now());

            ratingRepository.save(rating);
        }catch(BadRequestException ex){
            throw new BadRequestException("Something went wrong saving rates:"+ex.getMessage());
        }
    }


    @Override
    @CacheEvict(value = "ratings", key = "#userId")
    public void removeRating(Integer movieId, Integer userId) {
        try{
            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new BadRequestException("Movie not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            Rating rating = ratingRepository.findByMovieAndUser(movie, user)
                    .orElseThrow(() -> new BadRequestException("Rating not found for this movie"));

            ratingRepository.delete(rating);
        }catch (BadRequestException ex){
            throw new BadRequestException("Something went wrong removing rating:"+ex.getMessage());
        }
    }


    @Cacheable(value = "ratings", key = "#userId")
    @Override
    public List<Rating> getUserRatings(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        return ratingRepository.findAllByUserId(userId);
    }
}

