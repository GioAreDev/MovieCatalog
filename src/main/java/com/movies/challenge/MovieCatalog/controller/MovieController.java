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
import com.movies.challenge.MovieCatalog.exceptions.BadRequestException;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addMovies")
    public ResponseEntity<String> addMovies(@RequestBody Movie movie) {
        try {
            service.add(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie has been created successfully");
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateMovies")
    public ResponseEntity<String> updateMovies(@RequestBody Movie movie) {
        try {
            service.update(movie);
            return ResponseEntity.status(HttpStatus.OK).body("Movie has been updated successfully");
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteMovies/{movieId}")
    public ResponseEntity<String> deleteMovies(@PathVariable Integer movieId) {
        try {
            service.delete(movieId);
            return ResponseEntity.status(HttpStatus.OK).body("Movie has been deleted successfully");
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> getMovies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Year year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        try {
            Page<Movie> movies = service.findAll(search, category, year, page, size, sortBy, direction);
            return ResponseEntity.ok(movies);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
