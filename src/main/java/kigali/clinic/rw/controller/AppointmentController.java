package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

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
    public ResponseEntity<?> saveAppointmentNoDoubleBooking(@RequestBody Appointment appointment) {
        String result = appointmentService.saveAppointmentNoDoubleBooking(appointment);
        if ("Appointment date cannot be in the past".equals(result)) {
            return ResponseEntity.status(400).body(result);
        } else if ("Doctor is already booked on that date".equals(result)) {
            return ResponseEntity.status(409).body(result);
        } else {
            return ResponseEntity.ok(appointment);
        }
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable UUID id) {
        return appointmentService.getAppointmentById(id);
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable UUID id, @RequestBody Appointment details) {
        return appointmentService.updateAppointment(id, details);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(
            @PathVariable UUID id,
            @RequestParam AppointmentStatus status
    ) {
        String result = appointmentService.updateAppointmentStatus(id, status);
        if (result.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else if (result.startsWith("Cannot change status from")) {
            return ResponseEntity.status(400).body(result);
        } else {
            return ResponseEntity.status(200).body(result);
        }
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<String> rescheduleAppointment(
            @PathVariable UUID id,
            @RequestParam LocalDate newDate
    ) {
        String result = appointmentService.rescheduleAppointment(id, newDate);
        if (result.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(404).body(result);
        } else if (result.equals("A COMPLETED appointment cannot be rescheduled")
                || result.equals("A CANCELLED appointment cannot be rescheduled")
                || result.equals("Appointment date cannot be in the past")
                || result.equals("Appointment has no doctor assigned")) {
            return ResponseEntity.status(400).body(result);
        } else if (result.equals("Doctor is already booked on that date")) {
            return ResponseEntity.status(409).body(result);
        } else {
            return ResponseEntity.status(200).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
    }

    @GetMapping("/by-status")
    public List<Appointment> getAppointmentsByStatus(@RequestParam AppointmentStatus status) {
        return appointmentService.getAppointmentsByStatus(status);
    }

    @GetMapping("/between")
    public List<Appointment> getAppointmentsBetween(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
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

    @PatchMapping("/cancel-day")
    public String cancelDay(
            @RequestParam UUID doctorId,
            @RequestParam LocalDate date) {
        return appointmentService.cancelDoctorDay(doctorId, date);
    }

    @GetMapping("/page")
    public Page<Appointment> getAppointmentsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "date,desc") String sort) {
        String[] parts = sort.split(",");
        String prop = parts[0];
        if ("appointmentDate".equalsIgnoreCase(prop)) {
            prop = "date";
        }
        Sort.Direction dir = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1])) ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, prop));
        return appointmentService.getAllAppointmentsPaged(pageable);
    }

    @DeleteMapping("/cancelled-before")
    public String deleteCancelledBefore(
            @RequestParam LocalDate date) {
        return appointmentService.deleteCancelledBefore(date);
    }
}
