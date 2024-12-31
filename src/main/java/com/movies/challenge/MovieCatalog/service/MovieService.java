package com.movies.challenge.MovieCatalog.service;

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
public class MovieService implements  IMovieService<Movie, Integer>{

    @Autowired
    private IMovieRepository repository;

    @Override
    public void add(Movie entity) {
        repository.save(entity);

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
        Movie movieUpdated = repository.findById(movie.getMovieId()).get();
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
    }

    @Override
    public void delete(Integer id) {
        Movie movieDeleted = repository.findById(id).get();
        repository.delete(movieDeleted);
    }
}
