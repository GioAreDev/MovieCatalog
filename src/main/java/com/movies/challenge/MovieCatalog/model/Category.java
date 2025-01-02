package com.movies.challenge.MovieCatalog.model;


import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String name;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    public Category() {}

    public Category(Integer categoryId, String name, LocalDateTime createdDate) {
        this.categoryId = categoryId;
        this.name = name;
        this.createdDate = createdDate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
