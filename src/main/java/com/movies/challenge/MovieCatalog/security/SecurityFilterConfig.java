package com.movies.challenge.MovieCatalog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // Habilitar @PreAuthorize
public class SecurityFilterConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityFilterConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/register",  // Permitir acceso al registro
                                "/auth/login",     // Permitir acceso al login
                                "/v3/api-docs/**", // Permitir acceso a la documentación de Swagger
                                "/swagger-ui/**",  // Permitir acceso a los recursos de Swagger UI
                                "/swagger-ui.html" // Permitir acceso a la página principal de Swagger
                        ).permitAll()
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otro endpoint
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}


