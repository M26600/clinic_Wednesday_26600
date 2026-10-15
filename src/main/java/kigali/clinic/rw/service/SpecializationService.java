package kigali.clinic.rw.service;

import kigali.clinic.rw.Repository.SpecializationRepository;
import kigali.clinic.rw.domain.Specialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;

    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

    public Specialization getSpecializationById(UUID id) {
        return specializationRepository.findById(id).orElse(null);
    }

    public Specialization saveSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public Specialization updateSpecialization(UUID id, Specialization details) {
        Specialization spec = specializationRepository.findById(id).orElse(null);
        if (spec != null) {
            spec.setName(details.getName());
            return specializationRepository.save(spec);
        }
        return null;
    }

    public void deleteSpecialization(UUID id) {
        specializationRepository.deleteById(id);
    }

    public List<Specialization> getUnusedSpecializations() {
        return specializationRepository.findUnusedSpecializations();
    }
}
