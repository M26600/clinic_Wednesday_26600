package kigali.clinic.rw.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="sys_appointment")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    
    @Column(name="appointment_date")
    private LocalDateTime date;
    
    private String reason;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    
    // Ignore the 'appointments' list in Patient to prevent infinite recursion during JSON serialization
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="patient_id")
    @JsonIgnoreProperties({"appointments", "hibernateLazyInitializer", "handler"})
    private Patient patient;
    
    // Ignore the 'appointments' list in Doctor to prevent infinite recursion during JSON serialization
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="doctor_id")
    @JsonIgnoreProperties({"appointments", "hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    public Appointment() {
    }

    public Appointment(LocalDateTime date, String reason, AppointmentStatus status, Patient patient, Doctor doctor) {
        this.date = date;
        this.reason = reason;
        this.status = status;
        this.patient = patient;
        this.doctor = doctor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}