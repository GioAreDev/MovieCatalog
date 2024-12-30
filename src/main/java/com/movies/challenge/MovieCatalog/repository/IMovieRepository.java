package com.movies.challenge.MovieCatalog.repository;

import com.movies.challenge.MovieCatalog.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Year;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, Integer> {
    @Query("SELECT m FROM Movie m " +
            "WHERE (:search IS NULL OR m.name LIKE %:search% OR m.synopsis LIKE %:search%) " +
            "AND (:category IS NULL OR m.category.name = :category) " +
            "AND (:year IS NULL OR m.releaseYear = :year)")
    Page<Movie> findAllByFilters(
            @Param("search") String search,
            @Param("category") String category,
            @Param("year") Year year,
            Pageable pageable);
}
