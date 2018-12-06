package com.synoptic.weather.authentication.security.jwt;

import com.synoptic.weather.authentication.CustomUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication filter
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Authentication header
     */
    @Value("${authHeader}")
    private String authHeader;

    /**
     * Jwt token prefix
     */
    @Value("${tokenPrefix}")
    private String tokenPrefix;

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class);

    /**
     * Overrides method of its superclass
     *
     * @param filterChain filter chain
     * @param request     http servlet request
     * @param response    http servlet response
     * @throws ServletException when filter chain tries to do filter
     * @throws IOException      when filter chain tries to do filter
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = getJwtTokenFromRequest(request);

            if (StringUtils.hasText(jwtToken) && tokenProvider.isTokenValid(jwtToken)) {

                UserDetails userDetails = customUserDetailsService.loadUserById(tokenProvider.getUserIdFromJWT(jwtToken));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Gets jwt token from request
     *
     * @param request http servlet request
     * @return jwt token or null token invalid or not found
     */
    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader(authHeader);
        if (StringUtils.hasText(jwtToken) && jwtToken.startsWith(tokenPrefix)) {
            return jwtToken.substring(7);
        }
        return null;
    }
}
