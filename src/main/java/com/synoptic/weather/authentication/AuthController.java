package com.synoptic.weather.authentication;

import com.synoptic.weather.authentication.response.JwtAuthenticationResponse;
import com.synoptic.weather.authentication.security.jwt.JwtTokenProvider;
import com.synoptic.weather.model.entity.dto.UserDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Rest authentication controller
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Logger logger = Logger.getLogger(AuthController.class);

    /**
     * Component to provide jwt
     */
    @Autowired
    JwtTokenProvider tokenProvider;

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

        logger.info("Start registration of user: " + userDTO.toString());
        return authService.createUserAccount(userDTO);
    }
}
