package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Visit;
import com.example.demo.repository.VisitRepository;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(origins = "*")
public class VisitController {

    private final VisitRepository visitRepository;

    public VisitController(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @PostMapping
    public Visit createVisit(@RequestBody Visit visit) {
        return visitRepository.save(visit);
    }

    @GetMapping
    public List<Visit> getVisits() {
        return visitRepository.findAll();
    }

    @GetMapping("/patient/{patientId}")
    public List<Visit> getPatientVisits(
            @PathVariable Long patientId) {
        return visitRepository.findByPatientId(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Visit> getDoctorVisits(
            @PathVariable Long doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }
}