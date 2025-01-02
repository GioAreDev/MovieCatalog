package com.movies.challenge.MovieCatalog.service;

import com.movies.challenge.MovieCatalog.exceptions.BadRequestException;
import com.movies.challenge.MovieCatalog.service.interfaces.IMovieService;
import com.movies.challenge.MovieCatalog.model.Movie;
import com.movies.challenge.MovieCatalog.repository.IMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.Year;


@Service
public class MovieService implements IMovieService<Movie, Integer> {

    @Autowired
    private IMovieRepository repository;

    @Override
    public void add(Movie movie) {
        try {
            if (movie.getName() == null || movie.getName().isEmpty()) {
                throw new BadRequestException("Movie name is required");
            }
            if (movie.getReleaseYear() == null || movie.getReleaseYear().getValue() <= 0) {
                throw new BadRequestException("Invalid release year");
            }
            if (movie.getCategory() == null || movie.getCategory().getCategoryId() == null) {
                throw new BadRequestException("Category is required");
            }
            if (movie.getUser() == null || movie.getUser().getUserId() == null) {
                throw new BadRequestException("User is required");
            }

            repository.save(movie);
        }catch (BadRequestException ex) {
            throw new BadRequestException("Failed to add the movie. Please check the request data.");
        }
    }

    @Cacheable("movies")
    @Override
    public Page<Movie> findAll(String search, String category, Year year, int page, int size, String sortBy, String direction) {
        Sort sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.by(Sort.Order.desc(sortBy))
                : Sort.by(Sort.Order.asc(sortBy));

        Pageable pageable = PageRequest.of(page, size, sortDirection);

        return repository.findAllByFilters(search, category, year, pageable);
    }


    @Override
    public Movie update( Movie movie) {
        Movie movieUpdated = repository.findById(movie.getMovieId()).orElseThrow(()-> new BadRequestException("Movie ID must be provided for update."));
        try{
            if(movie.getCategory() != null){
                movieUpdated.setCategory(movie.getCategory());
            }
            if(movie.getCreatedDate() != null){
                movieUpdated.setCreatedDate(movie.getCreatedDate());
            }
            if(movie.getName() != null){
                movieUpdated.setName(movie.getName());
            }
            if(movie.getUser() != null){
                movieUpdated.setUser(movie.getUser());
            }
            if(movie.getSynopsis() != null){
                movieUpdated.setSynopsis(movie.getSynopsis());
            }
            if(movie.getReleaseYear() != null){
                movieUpdated.setReleaseYear(movie.getReleaseYear());
            }
            if(movie.getPosterImage() != null){
                movieUpdated.setPosterImage(movie.getPosterImage());
            }

            repository.save(movieUpdated);
            return movieUpdated;
        }catch(BadRequestException ex){
            throw new BadRequestException("Error updating movie:"+ex.getMessage());
        }


    }

    @Override
    public void delete(Integer id) {
        try{
           Movie movieDeleted= repository.findById(id).orElseThrow(()-> new BadRequestException("Movie could not be found!"));
           repository.delete(movieDeleted);
        }catch(BadRequestException ex){
            throw new BadRequestException("Error deleting movie:"+ex.getMessage());
        }
    }
}
