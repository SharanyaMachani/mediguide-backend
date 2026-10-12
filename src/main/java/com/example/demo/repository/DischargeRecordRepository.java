package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.DischargeRecord;

public interface DischargeRecordRepository
        extends JpaRepository<DischargeRecord, Long> {

    List<DischargeRecord> findByPatientId(Long patientId);

    List<DischargeRecord> findByDoctorId(Long doctorId);
}