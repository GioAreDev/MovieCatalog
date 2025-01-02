package com.movies.challenge.MovieCatalog.repository;

import com.movies.challenge.MovieCatalog.model.Movie;
import com.movies.challenge.MovieCatalog.model.Rating;
import com.movies.challenge.MovieCatalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRatingRepository extends JpaRepository<Rating,Integer> {

    boolean existsByMovieAndUser(Movie movie, User user);

    Optional<Rating> findByMovieAndUser(Movie movie, User user);

    @Query("SELECT r FROM Rating r WHERE r.user.userId = :userId")
    List<Rating> findAllByUserId(@Param("userId") Integer userId);
}
