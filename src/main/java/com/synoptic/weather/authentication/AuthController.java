package com.synoptic.weather.authentication;

import com.synoptic.weather.authentication.response.JwtAuthenticationResponse;
import com.synoptic.weather.authentication.security.jwt.JwtTokenProvider;
import com.synoptic.weather.model.repository.UserDao;
import com.synoptic.weather.model.entity.dto.UserDTO;
import com.synoptic.weather.exception.RestBadRequestException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ResourceBundle;

/**
 * Rest authentication controller
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Logger logger = Logger.getLogger(AuthController.class);

    /**
     * User data transfer object
     */
    @Autowired
    UserDao userDao;

    /**
     * Component to provide jwt
     */
    @Autowired
    JwtTokenProvider tokenProvider;

    /**
     * Object to provide data from property file
     */
    @Autowired
    private ResourceBundle resBundle;

    /**
     * Service to assist user authentication
     */
    @Autowired
    private AuthService authService;

    /**
     * Login user with jwt token
     *
     * @param userDTO user data transfer object (DTO)
     * @return response entity with jwt token
     */
    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDTO userDTO) {

        String jwtToken = tokenProvider.generateToken(authService.authenticateUser(userDTO));
        logger.info("Login user " + userDTO.toString() + " successfully");
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken));
    }

    /**
     * Register user
     *
     * @param userDTO user data transfer object (DTO)
     * @return response entity with user DTO or goes throw RestBadRequestException exception when DTO data is invalid
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        if (userDao.existsByUsername(userDTO.getUsername())) {
            logger.error("Username: " + userDTO.getUsername() + " is already taken. Process goes throw new RestBadRequestException");
            throw new RestBadRequestException(resBundle.getString("usernameIsTaken"));
        }
        if (userDao.existsByEmail(userDTO.getEmail())) {
            logger.error("Email: " + userDTO.getEmail() + " is already in use. Process goes throw new RestBadRequestException");
            throw new RestBadRequestException(resBundle.getString("emailInUse"));
        }
        logger.info("Start registration of user: " + userDTO.toString());
        return authService.createUserAccount(userDTO);
    }
}
