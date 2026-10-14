package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Diagnosis;
import com.example.demo.repository.DiagnosisRepository;

@RestController
@RequestMapping("/api/diagnoses")
@CrossOrigin(origins = "*")
public class DiagnosisController {

    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisController(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @PostMapping
    public Diagnosis createDiagnosis(@RequestBody Diagnosis diagnosis) {
        return diagnosisRepository.save(diagnosis);
    }

    @GetMapping("/visit/{visitId}")
    public List<Diagnosis> getByVisit(
            @PathVariable Long visitId) {
        return diagnosisRepository.findByVisitId(visitId);
    }
}