package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Prescription;
import com.example.demo.repository.PrescriptionRepository;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionController(
            PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @PostMapping
    public Prescription createPrescription(
            @RequestBody Prescription prescription) {

        return prescriptionRepository.save(prescription);
    }

    @GetMapping("/visit/{visitId}")
    public List<Prescription> getByVisit(
            @PathVariable Long visitId) {

        return prescriptionRepository.findByVisitId(visitId);
    }
}
