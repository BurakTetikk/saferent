package com.saferent.security.jwt;

import com.saferent.exception.message.ErrorMessage;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);


    @Value("${saferent.app.jwtSecret}")
    private String jwtSecret;

    @Value("${saferent.app.jwtExpirationMs}")
    private Long jwtExpirationMs ;



    // Generate JWT Token
    public String generateJwtToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }


    // JWT Token içinden email bilgisini almak
    public String getEmailFromJwtToken(String token) {

        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }




    // JWT Valide
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            LOGGER.error(String.format(ErrorMessage.JWT_TOKEN_ERROR_MESSAGE, e.getMessage()));
        }

        return false;
    }





}
