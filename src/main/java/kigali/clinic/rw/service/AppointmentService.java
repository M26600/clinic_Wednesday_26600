package kigali.clinic.rw.service;

import kigali.clinic.rw.Repository.AppointmentRepository;
import kigali.clinic.rw.domain.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
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

    public Appointment updateAppointment(UUID id, Appointment details) {
        Appointment apt = appointmentRepository.findById(id).orElse(null);
        if (apt != null) {
            apt.setDate(details.getDate());
            apt.setReason(details.getReason());
            return appointmentRepository.save(apt);
        }
        return null;
    }

    public String updateAppointmentStatus(UUID id, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            return "The appointment with that id does not exist";
        }
        AppointmentStatus current = appointment.getStatus();
        boolean allowed = false;
        if (current == AppointmentStatus.SCHEDULED) {
            if (newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED) {
                allowed = true;
            }
        } else if (current == AppointmentStatus.CONFIRMED) {
            if (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED) {
                allowed = true;
            }
        }
        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
        return "Appointment status updated successfully";
    }

    public String rescheduleAppointment(UUID id, LocalDate newDate) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            return "The appointment with that id does not exist";
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return "A COMPLETED appointment cannot be rescheduled";
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return "A CANCELLED appointment cannot be rescheduled";
        }
        if (newDate.isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            return "Appointment has no doctor assigned";
        }
        LocalDateTime start = newDate.atStartOfDay();
        LocalDateTime end = newDate.atTime(23, 59, 59, 999999999);
        boolean exists = appointmentRepository.existsByDoctorIdAndDateBetweenAndStatusNotAndIdNot(
                appointment.getDoctor().getId(),
                start,
                end,
                AppointmentStatus.CANCELLED,
                appointment.getId()
        );
        if (exists) {
            return "Doctor is already booked on that date";
        }
        if (appointment.getDate() != null) {
            appointment.setDate(newDate.atTime(appointment.getDate().toLocalTime()));
        } else {
            appointment.setDate(newDate.atStartOfDay());
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
        return "Appointment rescheduled successfully";
    }

    public String saveAppointmentNoDoubleBooking(Appointment appointment) {
        if (appointment.getDate() != null && appointment.getDate().toLocalDate().isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            boolean exists = appointmentRepository.existsByDoctorIdAndDateAndStatusNot(
                    appointment.getDoctor().getId(),
                    appointment.getDate(),
                    AppointmentStatus.CANCELLED
            );
            if (exists) {
                return "Doctor is already booked on that date";
            }
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

    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59, 999999999);
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

    public Page<Appointment> getDoctorAppointmentsPaged(UUID doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorId(doctorId, pageable);
    }

    public List<Object[]> getAppointmentStatsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }

    @Transactional
    public String cancelDoctorDay(UUID doctorId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59, 999999999);
        int cancelled = appointmentRepository.cancelDoctorAppointmentsOnDate(doctorId, start, end);
        return cancelled + " appointments cancelled";
    }

    public Page<Appointment> getAllAppointmentsPaged(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    @Transactional
    public String deleteCancelledBefore(LocalDate date) {
        LocalDateTime dateTime = date.atStartOfDay();
        int deleted = appointmentRepository.deleteCancelledAppointmentsBefore(dateTime);
        return deleted + " appointments deleted";
    }
}
