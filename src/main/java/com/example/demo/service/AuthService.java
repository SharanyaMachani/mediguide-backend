
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

        if (role != Role.PATIENT) {
            throw new RuntimeException(
                    "Public registration is allowed for patients only"
            );
        }

        if (email == null || email.isBlank()
                || password == null || password.length() < 8) {
            throw new RuntimeException(
                    "Enter a valid email and a password of at least 8 characters"
            );
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(email, hashedPassword, Role.PATIENT);

        return userRepository.save(user);
    }

    public User login(String email, String password, Role role) {

        User user = userRepository.findByEmail(email)
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