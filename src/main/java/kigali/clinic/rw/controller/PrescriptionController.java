package kigali.clinic.rw.controller;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/save")
    public ResponseEntity<String> savePrescription(
            @RequestParam UUID appointmentId,
            @RequestBody Prescription prescription
    ) {
        String result = prescriptionService.savePrescription(appointmentId, prescription);
        if (result.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else if (result.equals("Prescriptions are only for COMPLETED appointments")) {
            return ResponseEntity.status(400).body(result);
        } else {
            return ResponseEntity.status(201).body(result);
        }
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionsByAppointment(@PathVariable UUID appointmentId) {
        Appointment appointment = prescriptionService.getAppointmentById(appointmentId);
        if (appointment == null) {
            return ResponseEntity.status(404).body("The appointment with that id does not exist");
        }
        List<Prescription> list = prescriptionService.getPrescriptionsByAppointment(appointment);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable String ssNumber) {
        List<Prescription> list = prescriptionService.getPrescriptionsByPatientSsNumber(ssNumber);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePrescription(
            @PathVariable UUID id,
            @RequestBody Prescription details
    ) {
        String result = prescriptionService.updatePrescription(id, details);
        if (result.equals("The prescription with that id does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else {
            return ResponseEntity.status(200).body(result);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable UUID id) {
        String result = prescriptionService.deletePrescription(id);
        if (result.equals("The prescription with that id does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else {
            return ResponseEntity.status(200).body(result);
        }
    }
}
