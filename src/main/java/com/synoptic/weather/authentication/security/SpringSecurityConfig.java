package com.synoptic.weather.authentication.security;

import com.synoptic.weather.authentication.CustomUserDetailsService;
import com.synoptic.weather.authentication.security.jwt.JwtAuthenticationEntryPoint;
import com.synoptic.weather.authentication.security.jwt.JwtAuthenticationFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring security configuration object
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = Logger.getLogger(SpringSecurityConfig.class);

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    /**
     * Jwt authentication filter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Builds security configurations for current API
     *
     * @param authenticationManagerBuilder object to build and manage current API configuration
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        logger.info("Authentication manager builder is configured = " + authenticationManagerBuilder.isConfigured());
    }

    /**
     * Gets object to authenticate user by credentials
     *
     * @return super class authentication manager bean
     * @throws Exception when superclass method called
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {

        logger.info("Authentication manager bean = " + BeanIds.AUTHENTICATION_MANAGER);
        return super.authenticationManagerBean();
    }

    /**
     * Entity to encode user password
     *
     * @return cryptologic password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures user session and sets access via urls for authenticated and unauthorised users
     * Adds custom JwtAuthenticationFilter JWT security filter
     *
     * @param http spring http security
     * @throws Exception when called headers() method
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().disable().cors()
                .and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/api/weather/**", "/api/users/**")
                .permitAll()
                .anyRequest()
                .authenticated();
        logger.info("Configure access via url by " + http);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        logger.info("Add custom JWT security filter with jwt token prefix: " + jwtAuthenticationFilter().getTokenPrefix());
    }
}
