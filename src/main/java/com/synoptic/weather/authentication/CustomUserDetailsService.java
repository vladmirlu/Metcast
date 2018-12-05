package com.synoptic.weather.authentication;

import com.synoptic.weather.database.dao.UserDao;
import com.synoptic.weather.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ResourceBundle;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Autowired
    private ResourceBundle resBundle;

    /**
     *Lets login with either username or email
     *
     * */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userDao.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(resBundle.getString("userNotFoundWith") + usernameOrEmail));

        return UserPrincipal.create(user);
    }

    /** This method is used by JwtAuthenticationFilter
     *
     * */
    @Transactional
    public UserDetails loadUserById(Long id) {

        User user = userDao.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(resBundle.getString("userNotFoundWithId") + id));
        return UserPrincipal.create(user);
    }


}

