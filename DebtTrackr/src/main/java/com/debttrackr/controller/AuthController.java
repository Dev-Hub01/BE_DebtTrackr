package com.debttrackr.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth APIs", description = "Login and Register")

public class AuthController {


        @GetMapping("/me")
        public Object currentUser(Authentication authentication) {
            return authentication.getPrincipal();
        }

    @GetMapping("/google")
    @Hidden  // it's hide endpoints info in swagger
    public void googleLogin(
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect("/oauth2/authorization/google");
    }

}
