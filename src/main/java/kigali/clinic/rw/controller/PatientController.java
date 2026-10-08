package kigali.clinic.rw.controller;

import kigali.clinic.rw.service.PatientService;
import kigali.clinic.rw.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private kigali.clinic.rw.service.DoctorService doctorService;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable UUID id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable UUID id, @RequestBody Patient patientDetails) {
        Patient patient = patientService.getPatientById(id);
        if (patient != null) {
            patient.setNames(patientDetails.getNames());
            patient.setPhone(patientDetails.getPhone());
            return patientService.savePatient(patient);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
    }

    @GetMapping("/by-last-name")
    public List<Patient> getPatientsByLastName(@RequestParam String lastName) {
        return patientService.getPatientsByLastName(lastName);
    }

    @GetMapping("/of-doctor/{doctorId}")
    public org.springframework.http.ResponseEntity<?> getPatientsOfDoctor(@PathVariable(value="doctorId") UUID doctorId) {
        Object doc = doctorService.getDoctorById(doctorId);
        if (doc == null) {
            String msg = "The doctor with that id does not exist";
            return org.springframework.http.ResponseEntity.status(404).body(msg);
        }
        
        List<Patient> pts = patientService.getPatientsOfDoctor(doctorId);
        return org.springframework.http.ResponseEntity.ok(pts);
    }
}
