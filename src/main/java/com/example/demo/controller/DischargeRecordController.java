package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.DischargeRecord;
import com.example.demo.entity.User;
import com.example.demo.repository.DischargeRecordRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AiSimplificationService;
import com.example.demo.service.DischargeValidationService;
import com.example.demo.service.TeluguTranslationService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/discharge")
@CrossOrigin(origins = "*")
public class DischargeRecordController {

    private final DischargeRecordRepository dischargeRecordRepository;
    private final UserRepository userRepository;
    private final AiSimplificationService aiSimplificationService;
    private final TeluguTranslationService teluguTranslationService;
    private final DischargeValidationService validationService;

    public DischargeRecordController(
            DischargeRecordRepository dischargeRecordRepository,
            UserRepository userRepository,
            AiSimplificationService aiSimplificationService,
            TeluguTranslationService teluguTranslationService,
            DischargeValidationService validationService) {

        this.dischargeRecordRepository = dischargeRecordRepository;
        this.userRepository = userRepository;
        this.aiSimplificationService = aiSimplificationService;
        this.teluguTranslationService = teluguTranslationService;
        this.validationService = validationService;
    }

    @PostMapping
    public DischargeRecord createDischarge(
            @RequestBody DischargeRecord record) {

        return dischargeRecordRepository.save(record);
    }

    @GetMapping
    public List<DischargeRecord> getAllDischarges() {

        return dischargeRecordRepository.findAll();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientDischarges(
            @PathVariable Long patientId,
            Authentication authentication) {

        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))) {
            String email = authentication.getName();
            User currentUser = userRepository.findByEmailIgnoreCase(email).orElse(null);
            if (currentUser != null && !currentUser.getId().equals(patientId)) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN)
                        .body(java.util.Map.of("message", "Access denied: You can only view your own records."));
            }
        }

        return ResponseEntity.ok(dischargeRecordRepository.findByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public List<DischargeRecord> getDoctorDischarges(
            @PathVariable Long doctorId) {

        return dischargeRecordRepository.findByDoctorId(doctorId);
    }


    // AI SIMPLIFICATION
    @PostMapping("/{id}/simplify")
    public DischargeRecord simplifyDischarge(
            @PathVariable Long id) {

        DischargeRecord record =
                dischargeRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Discharge record not found"));

        String simplified =
                aiSimplificationService.simplifyInstructions(
                        record.getOriginalInstructions());

        record.setSimplifiedInstructions(simplified);

        return dischargeRecordRepository.save(record);
    }

    // TELUGU TRANSLATION
    @PostMapping("/{id}/translate")
    public DischargeRecord translateToTelugu(
            @PathVariable Long id) {

        DischargeRecord record =
                dischargeRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Discharge record not found"));

        String telugu =
                teluguTranslationService.translateToTelugu(
                        record.getSimplifiedInstructions());

        record.setTeluguInstructions(telugu);

        return dischargeRecordRepository.save(record);
    }

    // VALIDATION
    @PostMapping("/{id}/validate")
    public String validateDischarge(
            @PathVariable Long id) {

        DischargeRecord record =
                dischargeRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Discharge record not found"));

        boolean valid =
                validationService.validateInstructions(
                        record.getOriginalInstructions(),
                        record.getSimplifiedInstructions());

        if (valid) {
            return "Validation successful";
        }

        return "Validation failed";
    }

    
    // DOCTOR APPROVAL
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveDischarge(
            @PathVariable Long id) {

        DischargeRecord record =
                dischargeRecordRepository.findById(id)
                        .orElse(null);

        if (record == null) {
            return ResponseEntity.notFound().build();
        }

        if (record.getSimplifiedInstructions() == null
                || record.getSimplifiedInstructions().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Cannot approve: simplified instructions are missing.");
        }

        if (record.getTeluguInstructions() == null
                || record.getTeluguInstructions().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Cannot approve: Telugu translation is missing.");
        }

        boolean valid = validationService.validateInstructions(
                record.getOriginalInstructions(),
                record.getSimplifiedInstructions());

        if (!valid) {
            return ResponseEntity.badRequest()
                    .body("Cannot approve: validation failed.");
        }

        record.setApproved(true);

        return ResponseEntity.ok(
                dischargeRecordRepository.save(record));
    }
}