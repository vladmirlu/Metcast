package com.synoptic.weather.authentication;

import com.synoptic.weather.model.entity.User;
import com.synoptic.weather.provider.EntityProviderBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Customised spring security user details service
 * */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(CustomUserDetailsService.class);

    @Autowired
    private EntityProviderBuilder entityProviderBuilder;

    /**
     *Lets user login with either username or email
     *
     * @param usernameOrEmail  user username or email
     * @return user principal if user input data is valid
     * @throws UsernameNotFoundException appears when user username or email is invalid
     * */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = entityProviderBuilder.getUserByUsernameOrEmailOrError(usernameOrEmail);

        return UserPrincipal.create(user);
    }

    /** This method is used by JwtAuthenticationFilter
     * @param userId current user id
     * @return user principal by id
     * */
    @Transactional
    public UserDetails loadUserById(Long userId) {

        User user = entityProviderBuilder.getUserByIdOrError(userId);

        return UserPrincipal.create(user);
    }


}

