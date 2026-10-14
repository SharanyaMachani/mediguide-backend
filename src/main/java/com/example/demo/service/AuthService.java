
package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User register(String email, String password, Role role) {

        if (role == null || (role != Role.PATIENT && role != Role.DOCTOR)) {
            throw new RuntimeException("Role must be either PATIENT or DOCTOR");
        }

        if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".")) {
            throw new RuntimeException("Please enter a valid email address");
        }

        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }

        String cleanEmail = email.trim().toLowerCase();

        if (userRepository.findByEmailIgnoreCase(cleanEmail).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(cleanEmail, hashedPassword, role);

        return userRepository.save(user);
    }

    public User login(String email, String password, Role role) {

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new RuntimeException("Invalid email or password");
        }

        String cleanEmail = email.trim().toLowerCase();

        User user = userRepository.findByEmailIgnoreCase(cleanEmail)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (user.getRole() != role) {
            throw new RuntimeException("Invalid role");
        }

        return user;
    }
}