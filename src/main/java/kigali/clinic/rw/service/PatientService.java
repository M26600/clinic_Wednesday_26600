package kigali.clinic.rw.service;

import kigali.clinic.rw.Repository.PatientRepository;
import kigali.clinic.rw.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(UUID id) {
        return patientRepository.findById(id).orElse(null);
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(UUID id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient != null) {
            patient.setFirstName(patientDetails.getFirstName());
            patient.setLastName(patientDetails.getLastName());
            patient.setPhone(patientDetails.getPhone());
            patient.setSsNumber(patientDetails.getSsNumber());
            return patientRepository.save(patient);
        }
        return null;
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Patient> getPatientsOfDoctor(UUID doctorId) {
        return patientRepository.findPatientsByDoctorId(doctorId);
    }

    public List<Patient> getFrequentPatients(long min) {
        return patientRepository.findFrequentPatients(min);
    }
}
