package com.synoptic.weather.authentication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class AuthenticationController {

    @RequestMapping("/api/authentication/login")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/api/authentication/logout")
    public void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }

}
