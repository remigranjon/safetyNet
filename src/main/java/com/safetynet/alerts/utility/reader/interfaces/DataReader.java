package com.safetynet.alerts.utility.reader.interfaces;

import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Person;

import java.util.Set;

public interface DataReader {
    Set<Person> readPersons();
    Set<FireStation> readFireStations();
    Set<MedicalRecord> readMedicalRecords();
}
