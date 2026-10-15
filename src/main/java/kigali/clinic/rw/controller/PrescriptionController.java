package kigali.clinic.rw.controller;

import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
