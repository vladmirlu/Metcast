package com.synoptic.weather.authentication.security.jwt;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication entry point
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * Overrides method of its superclass and sends error message about unauthorised user status
     *
     * @param httpServletRequest  http servlet request
     * @param httpServletResponse http servlet response
     * @param authExc             authentication exception
     * @throws IOException when response sends error message
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException authExc) throws IOException {
        logger.error("Responding with unauthorized error. Message = " + authExc.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authExc.getMessage());
    }
}