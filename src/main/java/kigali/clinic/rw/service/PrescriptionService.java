package kigali.clinic.rw.service;

import kigali.clinic.rw.Repository.AppointmentRepository;
import kigali.clinic.rw.Repository.PrescriptionRepository;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public String savePrescription(UUID appointmentId, Prescription prescription) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            return "The appointment with that id does not exist";
        }
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
        }
        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);
        prescriptionRepository.save(prescription);
        return "Prescription saved successfully";
    }

    public Appointment getAppointmentById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElse(null);
    }

    public List<Prescription> getPrescriptionsByAppointment(Appointment appointment) {
        return prescriptionRepository.findByAppointment(appointment);
    }

    public List<Prescription> getPrescriptionsByPatientSsNumber(String ssNumber) {
        return prescriptionRepository.findByPatientSsNumber(ssNumber);
    }

    public String updatePrescription(UUID id, Prescription details) {
        Prescription prescription = prescriptionRepository.findById(id).orElse(null);
        if (prescription == null) {
            return "The prescription with that id does not exist";
        }
        prescription.setMedicineName(details.getMedicineName());
        prescription.setDosage(details.getDosage());
        prescription.setDurationDays(details.getDurationDays());
        prescriptionRepository.save(prescription);
        return "Prescription updated successfully";
    }

    public String deletePrescription(UUID id) {
        Prescription prescription = prescriptionRepository.findById(id).orElse(null);
        if (prescription == null) {
            return "The prescription with that id does not exist";
        }
        prescriptionRepository.delete(prescription);
        return "Prescription deleted successfully";
    }
}
