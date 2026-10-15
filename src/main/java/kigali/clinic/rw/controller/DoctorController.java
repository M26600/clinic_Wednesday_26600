package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.DoctorService;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SpecializationService specializationService;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.saveDoctor(doctor);
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable UUID id) {
        return doctorService.getDoctorById(id);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctorDetails) {
        return doctorService.updateDoctor(id, doctorDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
    }

    @PutMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> addSpecializationToDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID specializationId
    ) {
        String result = doctorService.addSpecializationToDoctor(doctorId, specializationId);
        if (result.equals("The doctor with that id does not exist") || result.equals("That specialization does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else if (result.equals("The doctor already has that specialization")) {
            return ResponseEntity.status(409).body(result);
        } else {
            return ResponseEntity.status(200).body(result);
        }
    }

    @DeleteMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> removeSpecializationFromDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID specializationId
    ) {
        String result = doctorService.removeSpecializationFromDoctor(doctorId, specializationId);
        if (result.equals("The doctor with that id does not exist") || result.equals("That specialization does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else if (result.equals("The doctor does not have that specialization")) {
            return ResponseEntity.status(409).body(result);
        } else {
            return ResponseEntity.status(200).body(result);
        }
    }

    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<String> changeDoctorOffice(
            @PathVariable UUID doctorId,
            @RequestParam String officeNumber
    ) {
        String result = doctorService.changeDoctorOffice(doctorId, officeNumber);
        if (result.equals("The doctor with that id does not exist") || result.equals("The office with that office number does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else if (result.equals("That office is taken by other doctor")) {
            return ResponseEntity.status(409).body(result);
        } else {
            return ResponseEntity.status(200).body(result);
        }
    }

    @GetMapping("/by-specialization")
    public List<Doctor> getDoctorsBySpecialization(@RequestParam(value = "name") String name) {
        return doctorService.getDoctorsBySpecialization(name);
    }

    @GetMapping("/without-office")
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorService.getDoctorsWithoutOffice();
    }

    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<?> getDoctorAgenda(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate date
    ) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        if (doctor == null) {
            return ResponseEntity.status(404).body("The doctor with that id does not exist");
        }
        List<Appointment> agenda = doctorService.getDoctorAgenda(doctorId, date);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/search")
    public List<Doctor> searchDoctorsByName(@RequestParam String name) {
        return doctorService.searchDoctorsByName(name);
    }
}
