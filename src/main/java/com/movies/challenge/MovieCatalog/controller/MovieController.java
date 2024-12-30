package com.movies.challenge.MovieCatalog.controller;

import com.movies.challenge.MovieCatalog.model.Movie;
import com.movies.challenge.MovieCatalog.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.Year;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addMovies")
    public ResponseEntity<String> addMovies(@RequestBody Movie movie){
        service.add(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body("Movie has been created successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateMovies")
    public ResponseEntity<String> updateMovies(@RequestBody Movie movie){
        service.update(movie);
        return ResponseEntity.status(HttpStatus.OK).body("Movie has been updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteMovies/{movieId}")
    public ResponseEntity<String> deleteMovies(@PathVariable Integer movieId){
        service.delete(movieId);
        return ResponseEntity.status(HttpStatus.OK).body("Movie has been deleted successfully");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<Page<Movie>> getMovies(
            @RequestParam(required = false) String search, // Search by name or synopsis
            @RequestParam(required = false) String category, // Filter by category
            @RequestParam(required = false) Year year, // Filter by release year
            @RequestParam(defaultValue = "0") int page, // Page number
            @RequestParam(defaultValue = "10") int size, // Page size
            @RequestParam(defaultValue = "createdDate") String sortBy, // Sort field
            @RequestParam(defaultValue = "asc") String direction // Sort direction
    ) {
        Page<Movie> movies = service.findAll(search, category, year, page, size, sortBy, direction);
        return ResponseEntity.ok(movies);
    }

}
