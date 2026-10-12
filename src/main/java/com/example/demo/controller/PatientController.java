package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {

private final UserRepository userRepository;

public PatientController(UserRepository userRepository) {
    this.userRepository = userRepository;
}

@GetMapping
public List<Map<String, Object>> getPatients() {

    List<Map<String, Object>> patients = new ArrayList<>();

    List<User> registeredPatients =
            userRepository.findByRole(Role.PATIENT);

    for (User user : registeredPatients) {
        Map<String, Object> patient = new HashMap<>();

        patient.put("id", user.getId());
        patient.put("email", user.getEmail());
        patient.put("role", user.getRole().name());
        patient.put("fullName", user.getEmail());

        patients.add(patient);
    }

    return patients;
}
}
