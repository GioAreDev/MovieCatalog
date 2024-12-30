package com.movies.challenge.MovieCatalog.dto;

import com.movies.challenge.MovieCatalog.model.User;

public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
    private String rol;


    public UserResponse(Integer userId, String username, String email,String rol) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.rol = rol;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
