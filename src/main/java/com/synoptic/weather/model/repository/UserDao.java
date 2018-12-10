package com.synoptic.weather.model.repository;

import com.synoptic.weather.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * User repository to interact with model
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


}
