package kigali.clinic.rw.service;

import kigali.clinic.rw.Repository.DoctorRepository;
import kigali.clinic.rw.domain.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private kigali.clinic.rw.Repository.OfficeRepository officeRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(UUID id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public Doctor saveDoctor(Doctor doctor) {
        if (doctor.getOffice() != null && doctor.getOffice().getId() != null) {
            kigali.clinic.rw.domain.Office existingOffice = officeRepository.findById(doctor.getOffice().getId()).orElse(null);
            doctor.setOffice(existingOffice);
        }
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(UUID id) {
        doctorRepository.deleteById(id);
    }
}
