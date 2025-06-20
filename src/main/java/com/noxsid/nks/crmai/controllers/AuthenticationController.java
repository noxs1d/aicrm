package com.noxsid.nks.crmai.controllers;


import com.noxsid.nks.crmai.auth.AuthenticationRequest;
import com.noxsid.nks.crmai.auth.AuthenticationResponse;
import com.noxsid.nks.crmai.auth.RegisterRequest;
import com.noxsid.nks.crmai.service.AuthenticationService;
import com.noxsid.nks.crmai.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserService userService;


    @PostMapping("/api/register")
    public ResponseEntity<String> apiregister(@RequestBody RegisterRequest request) {
        if(userService.existsByUsername(request.getUsername())){
            return ResponseEntity.badRequest().body("Name is already taken");
        }

        if(userService.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body("Email is already registered");
        }

        service.register(request);

        return ResponseEntity.ok("Registration is completed successfully");
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", response.getAccessToken())
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(Duration.ofHours(10))
                    .sameSite("Lax")
                    .build();
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", response.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(Duration.ofHours(70))
                    .sameSite("Lax")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, accessTokenCookie.toString(),
                    refreshTokenCookie.toString()).body((Map.of("message", "Login success")));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Error("Неверный логин или пароль"));
        }
    }

    @PostMapping("/refresh_token")
    public String refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

         AuthenticationResponse authenticationResponse = service.refreshToken(request,response);
        return "redirect:/";
    }
}
