package com.synoptic.weather.provider;

import com.synoptic.weather.exception.AppException;
import com.synoptic.weather.model.entity.Role;
import com.synoptic.weather.model.entity.RoleName;
import com.synoptic.weather.model.entity.dto.UserDTO;
import com.synoptic.weather.model.repository.RoleDao;
import com.synoptic.weather.model.repository.UserDao;
import com.synoptic.weather.model.repository.WeatherCardDao;
import com.synoptic.weather.model.entity.dto.WeatherCardDTO;
import com.synoptic.weather.model.entity.User;
import com.synoptic.weather.model.entity.WeatherCard;
import com.synoptic.weather.exception.ResourceNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Component to provide entities
 */
@Component
public class EntityProviderBuilder {

    private static final Logger logger = Logger.getLogger(EntityProviderBuilder.class);

    /**
     * User repository
     */
    @Autowired
    private UserDao userDao;

    /**
     * Weather card repository
     */
    @Autowired
    private WeatherCardDao cardDao;

    /**
     * Role repository
     */
    @Autowired
    private RoleDao roleDao;

    /**
     * Spring password encoder
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Object to provide data from property file
     */
    @Autowired
    private ResourceBundle resBundle;

    /**
     * Gets user by username
     *
     * @param username current user username
     * @return existing user or goes throw ResourceNotFoundException exception
     */
    public User getUserOrExit(String username) {

        logger.debug("The user " + username + " is providing");
        return userDao.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    /**
     * Gets weather card  by concrete location name
     *
     * @param location current location
     * @return existing weather card or goes throw ResourceNotFoundException exception
     */
    public WeatherCard getWeatherCardOrError(String location) {

        logger.debug("The geographic location " + location + " is providing");
        return cardDao.findWeatherCardByLocation(location)
                .orElseThrow(() -> new ResourceNotFoundException("WeatherCard", "location", location));
    }

    /**
     * Gets existing weather card or creates the new one and sets current user into
     *
     * @param location current location name
     * @param user     current user
     * @return existing weather card or new created
     */
    public WeatherCard getExistingCardOrNewCreated(String location, User user) {

        logger.debug("The user " + user + " and geographic location" + location + " are providing");
        return cardDao.findWeatherCardByLocation(location)
                .orElse(WeatherCard.builder().location(location).users(new ArrayList<>(Collections.singletonList(user))).build());
    }

    /**
     * Converts weather card to weather card data transfer object (DTO)
     *
     * @param weatherCard current weather card
     * @return new built weather card DTO
     */
    public WeatherCardDTO weatherCardToDTO(WeatherCard weatherCard) {

        logger.debug("The data transfer object of weather card " + weatherCard + " is creating");
        return WeatherCardDTO.builder().id(weatherCard.getId()).location(weatherCard.getLocation()).build();
    }

    /**
     * Prepare new user from user DTO with role USER and encode user password
     *
     * @param userDTO user DTO
     * @return  prepared user or goes throw AppException exception
     */
    public User getPreparedForCreationUser(UserDTO userDTO) {

        Role userRole = roleDao.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException(resBundle.getString("useRoleNotSet")));
        logger.debug("The new user to register " + userDTO + " with role " + RoleName.ROLE_USER.name() + " is creating");
        return User.builder().username(userDTO.getUsername()).email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(Collections.singleton(userRole)).build();
    }

    /**
     * Gets user by username or email name
     *
     * @param usernameOrEmail username or email
     * @return existing user or goes throw UsernameNotFoundException exception
     */
    public User getUserByUsernameOrEmailOrError(String usernameOrEmail) {

        logger.debug("The user " + usernameOrEmail + " is providing");
        return userDao.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(resBundle.getString("userNotFoundWith") + usernameOrEmail));
    }

    /**
     * Gets user by id
     *
     * @param userId user id
     * @return existing user or goes throw UsernameNotFoundException exception
     */
    public User getUserByIdOrError(Long userId) {

        logger.debug("The user with id: " + userId + " is providing");
        return userDao.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(resBundle.getString("userNotFoundWithId") + userId));
    }

}
