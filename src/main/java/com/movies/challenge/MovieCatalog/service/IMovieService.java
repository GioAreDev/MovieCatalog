package com.movies.challenge.MovieCatalog.service;

import org.springframework.data.domain.Page;

import java.time.Year;
import java.util.List;

public interface IMovieService <T,ID>{
    void add(T entity);
    Page<T> findAll(String search, String category, Year year, int page, int size, String sortBy, String direction);
    T update(T entity);
    void delete(ID id);
}
