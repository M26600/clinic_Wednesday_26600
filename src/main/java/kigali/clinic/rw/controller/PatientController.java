package kigali.clinic.rw.controller;

import kigali.clinic.rw.service.PatientService;
import kigali.clinic.rw.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import kigali.clinic.rw.service.DoctorService;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

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
        return patientService.updatePatient(id, patientDetails);
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
    public ResponseEntity<?> getPatientsOfDoctor(@PathVariable(value = "doctorId") UUID doctorId) {
        Object doc = doctorService.getDoctorById(doctorId);
        if (doc == null) {
            String msg = "The doctor with that id does not exist";
            return ResponseEntity.status(404).body(msg);
        }

        List<Patient> pts = patientService.getPatientsOfDoctor(doctorId);
        return ResponseEntity.ok(pts);
    }

    @GetMapping("/frequent")
    public List<Patient> getFrequentPatients(@RequestParam(defaultValue = "3") long min) {
        return patientService.getFrequentPatients(min);
    }
}
