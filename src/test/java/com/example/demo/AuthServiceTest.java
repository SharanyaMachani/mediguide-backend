package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    void testRegisterAndLoginPatientAndDoctor() {
        String testPatientEmail = "unit_patient_" + System.currentTimeMillis() + "@test.com";
        String testDoctorEmail = "unit_doctor_" + System.currentTimeMillis() + "@test.com";

        // Test Patient Registration
        User patient = authService.register(testPatientEmail, "StrongPassword123!", Role.PATIENT);
        assertNotNull(patient.getId());
        assertEquals(Role.PATIENT, patient.getRole());

        // Test Doctor Registration
        User doctor = authService.register(testDoctorEmail, "StrongDoctorPass123!", Role.DOCTOR);
        assertNotNull(doctor.getId());
        assertEquals(Role.DOCTOR, doctor.getRole());

        // Test Case-Insensitive Patient Login
        User loggedPatient = authService.login(testPatientEmail.toUpperCase(), "StrongPassword123!", Role.PATIENT);
        assertEquals(patient.getId(), loggedPatient.getId());

        // Test Case-Insensitive Doctor Login
        User loggedDoctor = authService.login(testDoctorEmail.toUpperCase(), "StrongDoctorPass123!", Role.DOCTOR);
        assertEquals(doctor.getId(), loggedDoctor.getId());

        // Test Duplicate Registration Prevention
        assertThrows(RuntimeException.class, () -> {
            authService.register(testPatientEmail, "AnotherPassword123!", Role.PATIENT);
        });

        // Test Invalid Role Login
        assertThrows(RuntimeException.class, () -> {
            authService.login(testPatientEmail, "StrongPassword123!", Role.DOCTOR);
        });

        // Test Wrong Password Login
        assertThrows(RuntimeException.class, () -> {
            authService.login(testPatientEmail, "WrongPassword!", Role.PATIENT);
        });
    }
}
