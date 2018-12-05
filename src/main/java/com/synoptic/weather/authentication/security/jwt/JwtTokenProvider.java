package com.synoptic.weather.authentication.security.jwt;

import com.synoptic.weather.authentication.UserPrincipal;
import io.jsonwebtoken.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
* Component to generate and provide jwt token
* */
@Component
public class JwtTokenProvider {

    private final Logger logger = Logger.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    /**
     * Generates jwt token
     *
     * @param authentication spring security authentication entity
     * @return string jwt token
     * */
    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     *Provides user id by jwtToken
     *
     * @param jwtToken user jwt jwtToken
     * @return current user id
     * */
    public Long getUserIdFromJWT(String jwtToken) {

        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     *Validates user token
     *
     * @param jwtToken user jwt token
     * @return true if user token is valid or false if not
     * */
    public boolean isTokenValid(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
