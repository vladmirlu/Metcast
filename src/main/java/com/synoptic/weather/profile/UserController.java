package com.synoptic.weather.profile;

import com.synoptic.weather.authentication.UserPrincipal;
import com.synoptic.weather.authentication.security.CurrentUser;
import com.synoptic.weather.model.repository.UserDao;
import com.synoptic.weather.model.entity.dto.UserDTO;
import com.synoptic.weather.model.entity.User;
import com.synoptic.weather.exception.ResourceNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Rest controller to serve user profile
 * */
@RestController
@RequestMapping("/api/users/")
public class UserController {

    @Autowired
    private UserDao userDao;

    private static final Logger logger = Logger.getLogger(UserController.class);
/**
 * Gets current user
 *
 * @param currentUser  current principal user
 * @return user data transfer object with current user data
 * */
    @GetMapping("current")
    @PreAuthorize("hasRole('USER')")
    public UserDTO getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    logger.info("The current user "+ currentUser.getUsername() + " is authorised ");
        return UserDTO.builder().id(currentUser.getId()).username(currentUser.getUsername()).build();
    }

    /**
     * Gets user profile
     *
     * @param username  current user username
     * @return user data transfer object with current user data
     * */
    @GetMapping("{username}")
    public UserDTO getUserProfile(@PathVariable(value = "username") String username) {

        User user = userDao.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        logger.debug("The user profile of "+ username + " is loaded ");
        return UserDTO.builder().username(user.getUsername()).email(user.getEmail()).build();
    }
}
