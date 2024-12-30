package com.movies.challenge.MovieCatalog.security;

import com.movies.challenge.MovieCatalog.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private  String secret_key;

    // Extraer el username (subject) del token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Validar el token
    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Verificar si el token ha expirado
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new java.util.Date());
    }

    // Extraer todos los claims del token JWT
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret_key) // Decodifica la clave secreta en Base64 si es necesario
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new IllegalStateException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalStateException("Error parsing JWT token: " + e.getMessage());
        }
    }


    // Generar un token JWT
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name()) // Agrega el rol como claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS512, secret_key)
                .compact();
    }
}



