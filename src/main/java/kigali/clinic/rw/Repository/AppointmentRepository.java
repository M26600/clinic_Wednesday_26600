package kigali.clinic.rw.Repository;

import kigali.clinic.rw.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import kigali.clinic.rw.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByDoctorIdAndStatusOrderByDateAsc(UUID doctorId, AppointmentStatus status);
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByDateAfter(java.time.LocalDateTime date);
    
    List<Appointment> findByReasonContainingIgnoreCase(String keyword);
    List<Appointment> findByStatusAndDateBefore(AppointmentStatus status, java.time.LocalDateTime date);
    long countByStatus(AppointmentStatus status);
    List<Appointment> findTop5ByDoctorIdOrderByDateDesc(UUID doctorId);

    List<Appointment> findByStatusOrderByDateAsc(AppointmentStatus status);
    List<Appointment> findByDateBetweenOrderByDateAsc(java.time.LocalDateTime start, java.time.LocalDateTime end);
    long countByDoctorIdAndDate(UUID doctorId, java.time.LocalDateTime date);

    @Query("SELECT a.patient.names, a.doctor.lastName, a.date FROM Appointment a WHERE a.status = 'PENDING' AND a.date < CURRENT_TIMESTAMP")
    List<Object[]> findPastPendingAppointments();

    @Query("SELECT a.doctor.lastName, COUNT(a) as aptCount FROM Appointment a GROUP BY a.doctor.lastName ORDER BY aptCount DESC")
    List<Object[]> countAppointmentsPerDoctor();

    @Query("SELECT a FROM Appointment a JOIN a.doctor d JOIN d.specializations s WHERE s.name = :specName")
    List<Appointment> findAppointmentsByDoctorSpecialization(@Param("specName") String specName);

    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();
}
