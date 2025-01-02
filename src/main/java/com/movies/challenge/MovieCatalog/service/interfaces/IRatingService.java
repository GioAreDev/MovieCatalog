package com.movies.challenge.MovieCatalog.service.interfaces;



import java.util.List;

public interface IRatingService <T,ID>{
    void rateMovie(Integer movieId, Integer userId, Byte ratingValue);
    void removeRating(Integer movieId, Integer userId);
    List<T> getUserRatings(Integer userId);
}
