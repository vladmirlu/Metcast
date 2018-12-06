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
 */
@Component
public class JwtTokenProvider {

    private final Logger logger = Logger.getLogger(JwtTokenProvider.class);

    /**
     * jwt secret key
     * */
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * jwt token expiration time in milliseconds
     * */
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    /**
     * Generates jwt token
     *
     * @param authentication spring security authentication entity
     * @return string jwt token
     */
    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        logger.info("Get UserPrincipal: " + ((UserPrincipal) authentication.getPrincipal()).getUsername());
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Provides user id by jwt
     *
     * @param jwt user json web token
     * @return current user id
     */
    public Long getUserIdFromJWT(String jwt) {

        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody();
        logger.debug("Get user id  by jwt = "+ jwt );
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Validates user token
     *
     * @param jwtToken user jwt token
     * @return true if user token is valid or false if not
     */
    public boolean isTokenValid(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            logger.info(" Jwt token "+ jwtToken.substring(10) + " is valid");
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature. token = " + jwtToken + "; Cached SignatureException: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token = " + jwtToken + "; Cached MalformedJwtException: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token = "  + jwtToken + "; Cached ExpiredJwtException: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token = "  + jwtToken + "; Cached UnsupportedJwtException: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty. Token = "  + jwtToken + "; Cached IllegalArgumentException: " + ex.getMessage());
        }
        logger.info("Invalid jwt token "+ jwtToken.substring(10));
        return false;
    }
}
