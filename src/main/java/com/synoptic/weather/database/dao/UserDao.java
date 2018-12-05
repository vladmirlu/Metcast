package com.synoptic.weather.database.dao;

import com.synoptic.weather.database.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * User repository to interact with database
 */
@Transactional
public interface UserDao extends CrudRepository<User, Long> {

    /**
     * Finds optional user by username or email
     *
     * @param username user username
     * @param email user email
     * */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Finds optional user by username
     *
     * @param username user username
     * */
    Optional<User> findByUsername(String username);

    /**
     * Checks if user exists by username
     *
     * @param username user username
     * */
    Boolean existsByUsername(String username);

    /**
     * Checks if user exists by email
     *
     * @param email user email
     * */
    Boolean existsByEmail(String email);
}
