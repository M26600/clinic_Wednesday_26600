package kigali.clinic.rw.service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.Repository.DoctorRepository;
import kigali.clinic.rw.Repository.OfficeRepository;
import kigali.clinic.rw.Repository.SpecializationRepository;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;

@Service

public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    public String addSpecializationToDoctor(UUID doctorId, UUID specializationId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return "The doctor with that id does not exist";
        }
        Specialization spec = specializationRepository.findById(specializationId).orElse(null);
        if (spec == null) {
            return "That specialization does not exist";
        }
        if (doctor.getSpecializations() == null) {
            doctor.setSpecializations(new HashSet<>());
        }
        if (doctor.getSpecializations().contains(spec)) {
            return "The doctor already has that specialization";
        }
        doctor.getSpecializations().add(spec);
        doctorRepository.save(doctor);
        return "Specialization added to the doctor";
    }

    public String removeSpecializationFromDoctor(UUID doctorId, UUID specializationId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return "The doctor with that id does not exist";
        }
        Specialization spec = specializationRepository.findById(specializationId).orElse(null);
        if (spec == null) {
            return "That specialization does not exist";
        }
        if (doctor.getSpecializations() == null || !doctor.getSpecializations().contains(spec)) {
            return "The doctor does not have that specialization";
        }
        doctor.getSpecializations().remove(spec);
        doctorRepository.save(doctor);
        return "Specialization removed from the doctor";
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(UUID id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public Doctor saveDoctor(Doctor doctor) {
        if (doctor.getOffice() != null && doctor.getOffice().getId() != null) {
            Office existingOffice = officeRepository.findById(doctor.getOffice().getId()).orElse(null);
            doctor.setOffice(existingOffice);
        }
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(UUID id) {
        doctorRepository.deleteById(id);
    }

    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepository.findBySpecializationName(name);
    }

    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepository.findDoctorsWithoutOffice();
    }
}
