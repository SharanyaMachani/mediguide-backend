package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class DischargeValidationService {

    public boolean validateInstructions(
            String original,
            String simplified) {

        if (original == null || simplified == null) {
            return false;
        }

        if (simplified.isBlank()) {
            return false;
        }

        return true;
    }
}