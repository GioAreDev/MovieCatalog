package com.movies.challenge.MovieCatalog.service;

import com.movies.challenge.MovieCatalog.exceptions.BadRequestException;
import com.movies.challenge.MovieCatalog.service.interfaces.IUserService;
import com.movies.challenge.MovieCatalog.model.User;
import com.movies.challenge.MovieCatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements IUserService<User,Integer> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public User register(User user) {

           try{

               if(user.getUsername()==null || user.getUsername().isEmpty()){
                   throw new BadRequestException("Username is required");
               }
               if(user.getEmail()==null || user.getEmail().isEmpty()){
                   throw new BadRequestException("Email is required");
               }
               if(user.getPasswordHash()==null || user.getPasswordHash().isEmpty()){
                   throw new BadRequestException("Password is required");
               }
               if(user.getRole()==null){
                  throw new BadRequestException("Role is required");
               }
               user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
               user.setCreatedDate(LocalDateTime.now());
               return userRepository.save(user);
           }catch (BadRequestException ex){
               throw new BadRequestException(ex.getMessage());
           }
    }
}


