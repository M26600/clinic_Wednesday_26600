package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.Repository.AppointmentRepository;
import kigali.clinic.rw.Repository.DoctorRepository;
import kigali.clinic.rw.Repository.OfficeRepository;
import kigali.clinic.rw.Repository.SpecializationRepository;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
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

    @Autowired
    private AppointmentRepository appointmentRepository;

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

    public String changeDoctorOffice(UUID doctorId, String officeNumber) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return "The doctor with that id does not exist";
        }
        List<Office> offices = officeRepository.findByOfficeNumber(officeNumber);
        if (offices.isEmpty()) {
            return "The office with that office number does not exist";
        }
        Office office = offices.get(0);
        if (doctorRepository.existsByOffice(office)) {
            return "That office is taken by other doctor";
        }
        doctor.setOffice(office);
        doctorRepository.save(doctor);
        return "Office changed successfully";
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

    public Doctor updateDoctor(UUID id, Doctor doctorDetails) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor != null) {
            doctor.setFirstName(doctorDetails.getFirstName());
            doctor.setLastName(doctorDetails.getLastName());
            doctor.setDateOfBirth(doctorDetails.getDateOfBirth());
            if (doctorDetails.getOffice() != null && doctorDetails.getOffice().getId() != null) {
                Office existingOffice = officeRepository.findById(doctorDetails.getOffice().getId()).orElse(null);
                doctor.setOffice(existingOffice);
            }
            return doctorRepository.save(doctor);
        }
        return null;
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

    public List<Appointment> getDoctorAgenda(UUID doctorId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59, 999999999);
        return appointmentRepository.findByDoctorIdAndDateBetweenAndStatusNot(doctorId, start, end, AppointmentStatus.CANCELLED);
    }

    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.searchByName(name);
    }
}
