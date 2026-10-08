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

    public void deleteSpecialization(UUID id) {
        specializationRepository.deleteById(id);
    }
}
