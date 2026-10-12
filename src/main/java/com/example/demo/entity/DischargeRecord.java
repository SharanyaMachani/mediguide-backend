package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "discharge_records")
public class DischargeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalInstructions;

    @Column(columnDefinition = "TEXT")
    private String simplifiedInstructions;

    @Column(columnDefinition = "TEXT")
    private String teluguInstructions;

    private boolean approved = false;

    public DischargeRecord() {
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getOriginalInstructions() {
        return originalInstructions;
    }

    public void setOriginalInstructions(String originalInstructions) {
        this.originalInstructions = originalInstructions;
    }

    public String getSimplifiedInstructions() {
        return simplifiedInstructions;
    }

    public void setSimplifiedInstructions(String simplifiedInstructions) {
        this.simplifiedInstructions = simplifiedInstructions;
    }

    public String getTeluguInstructions() {
        return teluguInstructions;
    }

    public void setTeluguInstructions(String teluguInstructions) {
        this.teluguInstructions = teluguInstructions;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}