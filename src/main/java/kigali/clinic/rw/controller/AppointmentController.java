package kigali.clinic.rw.controller;

import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.domain.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.saveAppointment(appointment);
    }

    @PostMapping("/save")
    public String saveAppointmentNoDoubleBooking(@RequestBody Appointment appointment) {
        return appointmentService.saveAppointmentNoDoubleBooking(appointment);
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable UUID id) {
        return appointmentService.getAppointmentById(id);
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable UUID id, @RequestBody Appointment details) {
        Appointment apt = appointmentService.getAppointmentById(id);
        if (apt != null) {
            apt.setDate(details.getDate());
            apt.setReason(details.getReason());
            apt.setStatus(details.getStatus());
            return appointmentService.saveAppointment(apt);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
    }

    @GetMapping("/by-status")
    public List<Appointment> getAppointmentsByStatus(@RequestParam kigali.clinic.rw.domain.AppointmentStatus status) {
        return appointmentService.getAppointmentsByStatus(status);
    }

    @GetMapping("/between")
    public List<Appointment> getAppointmentsBetween(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate start,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate end) {
        return appointmentService.getAppointmentsBetween(start, end);
    }

    @GetMapping("/pending/{doctorId}")
    public List<Appointment> getPendingAppointments(@PathVariable UUID doctorId) {
        return appointmentService.getPendingAppointments(doctorId);
    }

    @GetMapping("/past-pending")
    public List<Object[]> getPastPending() {
        return appointmentService.findPastPendingAppointments();
    }

    @GetMapping("/doctor-loads")
    public List<Object[]> getDoctorLoads() {
        return appointmentService.getDoctorLoads();
    }

    @GetMapping("/by-specialization/{specName}")
    public List<Appointment> getAppointmentsBySpec(@PathVariable String specName) {
        return appointmentService.getAppointmentsBySpec(specName);
    }

    @GetMapping("/doctor-page/{doctorId}")
    public Page<Appointment> getDoctorAppointmentsPaged(@PathVariable UUID doctorId) {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("date").ascending());
        return appointmentService.getDoctorAppointmentsPaged(doctorId, pageable);
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> getAppointmentsByStatusStats() {
        return appointmentService.getAppointmentStatsByStatus();
    }
}
