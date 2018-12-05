package com.synoptic.weather.profile;

import com.synoptic.weather.authentication.UserPrincipal;
import com.synoptic.weather.authentication.payload.UserIdentityAvailability;
import com.synoptic.weather.authentication.security.CurrentUser;
import com.synoptic.weather.database.dao.UserDao;
import com.synoptic.weather.database.dto.UserDTO;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.exception.ResourceNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserDao userDao;

    private static final Logger logger = Logger.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserDTO getCurrentUser(@CurrentUser UserPrincipal currentUser) {

        return UserDTO.builder().id(currentUser.getId()).username(currentUser.getUsername()).build();
    }

    @GetMapping("/users/{username}")
    public UserDTO getUserProfile(@PathVariable(value = "username") String username) {

        User user = userDao.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return UserDTO.builder().username(user.getUsername()).email(user.getEmail()).build();
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {

        Boolean isAvailable = !userDao.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {

        Boolean isAvailable = !userDao.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

}
