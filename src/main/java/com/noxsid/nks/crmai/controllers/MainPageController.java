package com.noxsid.nks.crmai.controllers;


import com.noxsid.nks.crmai.auth.AuthenticationRequest;
import com.noxsid.nks.crmai.auth.AuthenticationResponse;
import com.noxsid.nks.crmai.auth.RegisterRequest;
import com.noxsid.nks.crmai.service.AuthenticationService;
import com.noxsid.nks.crmai.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class MainPageController {

    private final UserService userService;
    private final AuthenticationService service;

    public MainPageController(UserService userService, AuthenticationService service) {
        this.userService = userService;
        this.service = service;
    }
    private final Logger logger = Logger.getLogger("Warning");
    @GetMapping("/")
    public String getMainPage(){
        logger.log(Level.WARNING, "Did not opened");
        return "index";
    }
    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new RegisterRequest());
        return "signup";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult bindingResult,
                           Model model) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // Check if username exists
        if (userService.existsByUsername(request.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Username is already taken");
            return "signup";
        }

        // Check if email exists
        if (userService.existsByEmail(request.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email is already registered");
            return "signup";
        }

        try {
            service.register(request);
            // Redirect to prevent form resubmission
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
            return "signup";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute AuthenticationRequest request, Model model, HttpServletResponse res) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            logger.log(Level.INFO, request.getUsername());
            logger.log(Level.INFO, request.getPassword());
            Cookie cookie = new Cookie("access_token", response.getAccessToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24*60*60);
            res.addCookie(cookie);
            return "redirect:/";
        }
        catch (Exception e){
            model.addAttribute("error", "Invalid username or password");
            logger.log(Level.WARNING, "Authentication failed: " + e.getMessage());
            return "login";
        }
    }
}

