package kigali.clinic.rw.controller;

import kigali.clinic.rw.service.SpecializationService;
import kigali.clinic.rw.domain.Specialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @GetMapping
    public List<Specialization> getAllSpecializations() {
        return specializationService.getAllSpecializations();
    }

    @PostMapping
    public Specialization createSpecialization(@RequestBody Specialization specialization) {
        return specializationService.saveSpecialization(specialization);
    }

    @GetMapping("/{id}")
    public Specialization getSpecializationById(@PathVariable UUID id) {
        return specializationService.getSpecializationById(id);
    }

    @PutMapping("/{id}")
    public Specialization updateSpecialization(@PathVariable UUID id, @RequestBody Specialization details) {
        Specialization existing = specializationService.getSpecializationById(id);
        if (existing != null) {
            existing.setName(details.getName());
            return specializationService.saveSpecialization(existing);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteSpecialization(@PathVariable UUID id) {
        specializationService.deleteSpecialization(id);
    }

    @GetMapping("/unused")
    public List<Specialization> getUnusedSpecializations() {
        List<Specialization> unused = specializationService.getUnusedSpecializations();
        // System.out.println(unused.size());
        return unused;
    }
}
