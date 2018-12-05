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

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDTO userDTO) {

        String jwtToken = tokenProvider.generateToken(authService.authenticateUser(userDTO));
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken));
    }

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
