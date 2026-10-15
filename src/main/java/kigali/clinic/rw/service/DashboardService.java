package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.Repository.AppointmentRepository;
import kigali.clinic.rw.Repository.DoctorRepository;
import kigali.clinic.rw.Repository.OfficeRepository;
import kigali.clinic.rw.Repository.PatientRepository;
import kigali.clinic.rw.Repository.PrescriptionRepository;
import kigali.clinic.rw.domain.AppointmentStatus;

@Service
public class DashboardService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public Map<String, Long> getDashboardSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59, 999999999);

        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("patients", patientRepository.count());
        summary.put("doctors", doctorRepository.count());
        summary.put("freeOffices", (long) officeRepository.findFreeOffices().size());
        summary.put("appointmentsToday", appointmentRepository.countByDateBetween(start, end));
        summary.put("scheduled", appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED));
        summary.put("prescriptions", prescriptionRepository.count());

        return summary;
    }
}
