package kigali.clinic.rw.controller;

import kigali.clinic.rw.service.DoctorService;
import kigali.clinic.rw.service.SpecializationService;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        Doctor existingDoctor = doctorService.getDoctorById(id);
        if (existingDoctor != null) {
            existingDoctor.setFirstName(doctorDetails.getFirstName());
            existingDoctor.setLastName(doctorDetails.getLastName());
            return doctorService.saveDoctor(existingDoctor);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
    }

    @PostMapping("/{doctorId}/specializations/{specializationId}")
    public Doctor assignSpecializationToDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID specializationId
    ) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        Specialization specialization = specializationService.getSpecializationById(specializationId);

        if (doctor != null && specialization != null) {
            doctor.getSpecializations().add(specialization);
            return doctorService.saveDoctor(doctor);
        }
        return null;
    }

    @GetMapping("/by-specialization")
    public List<Doctor> getDoctorsBySpecialization(@RequestParam(value="name") String name) {
        // String lower = name.toLowerCase(); // not needed anymore handled in query
        return doctorService.getDoctorsBySpecialization(name);
    }

    @GetMapping("/without-office")
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorService.getDoctorsWithoutOffice();
    }
}
