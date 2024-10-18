package com.safetynet.alerts.repository.interfaces;

import com.safetynet.alerts.model.entity.MedicalRecord;
import org.springframework.stereotype.Repository;
@Repository
public interface MedicalRecordRepository {

    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);
}
