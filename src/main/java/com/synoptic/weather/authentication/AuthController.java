package com.synoptic.weather.authentication;

import com.synoptic.weather.authentication.payload.JwtAuthenticationResponse;
import com.synoptic.weather.authentication.security.jwt.JwtTokenProvider;
import com.synoptic.weather.database.dao.UserDao;
import com.synoptic.weather.database.dto.UserDTO;
import com.synoptic.weather.exception.RestBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ResourceBundle;

/**
 * Rest authentication controller
 * */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserDao userDao;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private ResourceBundle resBundle;

    @Autowired
    private AuthService authService;

    /**
     * Login user with jwt token
     *
     * @param userDTO user data transfer object (DTO)
     * @return response entity with jwt token
     * */
    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDTO userDTO) {

        String jwtToken = tokenProvider.generateToken(authService.authenticateUser(userDTO));
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken));
    }

    /**
     * Register user
     *
     * @param userDTO user data transfer object (DTO)
     * @return response entity with user DTO or goes throw RestBadRequestException exception when DTO data is invalid
     * */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        if(userDao.existsByUsername(userDTO.getUsername())) {
           throw new RestBadRequestException(resBundle.getString("usernameIsTaken"));
        }
        if(userDao.existsByEmail(userDTO.getEmail())) {
            throw new RestBadRequestException(resBundle.getString("emailInUse"));
        }
       return authService.createUserAccount(userDTO);
    }
}
