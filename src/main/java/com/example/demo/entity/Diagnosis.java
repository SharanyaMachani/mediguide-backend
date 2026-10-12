
package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "diagnoses")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_id")
    private Integer id;

    @Column(name = "visit_id", nullable = false)
    private Integer visitId;

    @Column(name = "diagnosis_name", nullable = false)
    private String diagnosisName;

    @Column(name = "description")
    private String description;

    public Diagnosis() {
    }

    public Diagnosis(Integer visitId, String diagnosisName, String description) {
        this.visitId = visitId;
        this.diagnosisName = diagnosisName;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}