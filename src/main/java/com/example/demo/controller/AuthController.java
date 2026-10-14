package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            JwtService jwtService) {

        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) Role role,
            @RequestBody(required = false) AuthRequest body) {

        String targetEmail = (email != null && !email.isBlank()) ? email : (body != null ? body.getEmail() : null);
        String targetPassword = (password != null && !password.isBlank()) ? password : (body != null ? body.getPassword() : null);
        Role targetRole = (role != null) ? role : (body != null ? body.getRole() : null);

        User user = authService.register(targetEmail, targetPassword, targetRole);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) Role role,
            @RequestBody(required = false) AuthRequest body) {

        String targetEmail = (email != null && !email.isBlank()) ? email : (body != null ? body.getEmail() : null);
        String targetPassword = (password != null && !password.isBlank()) ? password : (body != null ? body.getPassword() : null);
        Role targetRole = (role != null) ? role : (body != null ? body.getRole() : null);

        User user = authService.login(targetEmail, targetPassword, targetRole);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}