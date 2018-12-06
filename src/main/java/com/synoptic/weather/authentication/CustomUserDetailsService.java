package com.synoptic.weather.authentication;

import com.synoptic.weather.model.repository.UserDao;
import com.synoptic.weather.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ResourceBundle;

/**
 * Customised spring security user details service
 * */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Autowired
    private ResourceBundle resBundle;

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
        User user = userDao.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(resBundle.getString("userNotFoundWith") + usernameOrEmail));

        return UserPrincipal.create(user);
    }

    /** This method is used by JwtAuthenticationFilter
     * @param userId current user id
     * @return user principal by id
     * */
    @Transactional
    public UserDetails loadUserById(Long userId) {

        User user = userDao.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(resBundle.getString("userNotFoundWithId") + userId));
        return UserPrincipal.create(user);
    }


}

