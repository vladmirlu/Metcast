package com.synoptic.weather.authentication;

import com.synoptic.weather.authentication.response.ApiResponse;
import com.synoptic.weather.model.repository.UserDao;
import com.synoptic.weather.model.entity.dto.UserDTO;
import com.synoptic.weather.model.entity.User;
import com.synoptic.weather.provider.EntityProviderBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ResourceBundle;

/**
 * Service to organise user data for registration and authentication
 * */
@Service
public class AuthService {

    private final Logger logger = Logger.getLogger(AuthService.class);

    /**
     * User repository
     * */
    @Autowired
    private UserDao userDao;

    /**
     * Object to provide data from property file
     * */
    @Autowired
    private ResourceBundle resBundle;

    /**
     * Spring authentication manager
     * */
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private EntityProviderBuilder entityProviderBuilder;

    /**
     * Creates user's account
     *
     * @param userDTO user data transfer  to provide user data
     * @return response entity with informational response of success user creation
     * */
    public ResponseEntity<?> createUserAccount(UserDTO userDTO){

        logger.debug("Creating account for user: " + userDTO.toString());
        User user =  userDao.save(entityProviderBuilder.getPreparedForCreationUser(userDTO));
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}").buildAndExpand(user.getUsername()).toUri();
        logger.debug("Account for user: " + user.toString() + " created");

        return ResponseEntity.created(location).body(new ApiResponse(true, resBundle.getString("registerSuccess")));
    }

    /**
     * Gets current user data spring security authentication entity
     *
     * @param userDTO user data transfer object (DTO)
     * @return current user data spring security authentication
     * */
    public Authentication authenticateUser(UserDTO userDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug("User: " + userDTO.toString() + " authenticated");

        return authentication;
    }
}
