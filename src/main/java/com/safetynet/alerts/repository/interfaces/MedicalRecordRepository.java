package com.safetynet.alerts.repository.interfaces;

import com.safetynet.alerts.model.entity.MedicalRecord;
import org.springframework.stereotype.Repository;
@Repository
public interface MedicalRecordRepository {

    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);

    boolean save(MedicalRecord medicalRecord);

    boolean update(MedicalRecord medicalRecord);

    boolean delete(String firstName, String lastName);
}
