package kigali.clinic.rw.service;

import kigali.clinic.rw.Repository.AppointmentRepository;
import kigali.clinic.rw.domain.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import kigali.clinic.rw.domain.AppointmentStatus;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(UUID id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public String saveAppointmentNoDoubleBooking(Appointment appointment) {
        long count = appointmentRepository.countByDoctorIdAndDate(appointment.getDoctor().getId(), appointment.getDate());
        if (count > 0) {
            return "Doctor is not available";
        }
        appointmentRepository.save(appointment);
        return "Saved";
    }

    public void deleteAppointment(UUID id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByDateAsc(status);
    }

    public List<Appointment> getAppointmentsBetween(java.time.LocalDate start, java.time.LocalDate end) {
        java.time.LocalDateTime startDateTime = start.atStartOfDay();
        java.time.LocalDateTime endDateTime = end.atTime(23, 59, 59, 999999999);
        return appointmentRepository.findByDateBetweenOrderByDateAsc(startDateTime, endDateTime);
    }

    public List<Object[]> findPastPendingAppointments() {
        return appointmentRepository.findPastPendingAppointments();
    }

    public List<Appointment> getPendingAppointments(UUID doctorId) {
        return appointmentRepository.findByDoctorIdAndStatusOrderByDateAsc(doctorId, AppointmentStatus.PENDING);
    }

    public List<Object[]> getDoctorLoads() {
        return appointmentRepository.countAppointmentsPerDoctor();
    }

    public List<Appointment> getAppointmentsBySpec(String specName) {
        return appointmentRepository.findAppointmentsByDoctorSpecialization(specName);
    }

    public org.springframework.data.domain.Page<Appointment> getDoctorAppointmentsPaged(UUID doctorId, org.springframework.data.domain.Pageable pageable) {
        return appointmentRepository.findByDoctorId(doctorId, pageable);
    }

    public List<Object[]> getAppointmentStatsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }
}
