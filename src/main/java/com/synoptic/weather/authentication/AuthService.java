package com.synoptic.weather.authentication;

import com.synoptic.weather.authentication.payload.ApiResponse;
import com.synoptic.weather.database.dao.RoleDao;
import com.synoptic.weather.database.dao.UserDao;
import com.synoptic.weather.database.dto.UserDTO;
import com.synoptic.weather.database.entity.Role;
import com.synoptic.weather.database.entity.RoleName;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Service to organise user data for registration and authentication
 * */
@Service
public class AuthService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private ResourceBundle resBundle;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Creates user's account
     *
     * @param userDTO user data transfer  to provide user data
     * @return response entity with informational response of success user creation
     * */
    public ResponseEntity<?> createUserAccount(UserDTO userDTO){

        Role userRole = roleDao.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException(resBundle.getString("useRoleNotSet")));

        User user = User.builder().username(userDTO.getUsername()).email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(Collections.singleton(userRole)).build();

        user = userDao.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

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
        return authentication;
    }
}
