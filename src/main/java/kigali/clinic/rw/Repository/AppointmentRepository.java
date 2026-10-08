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

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByDoctorIdAndStatusOrderByDateAsc(UUID doctorId, String status);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByDateAfter(java.time.LocalDateTime date);
    
    List<Appointment> findByReasonContainingIgnoreCase(String keyword);
    List<Appointment> findByStatusAndDateBefore(String status, java.time.LocalDateTime date);
    long countByStatus(String status);
    List<Appointment> findTop5ByDoctorIdOrderByDateDesc(UUID doctorId);

    @Query("SELECT a.patient.names, a.doctor.lastName, a.date FROM Appointment a WHERE a.status = 'PENDING' AND a.date < CURRENT_TIMESTAMP")
    List<Object[]> findPastPendingAppointments();

    @Query("SELECT a.doctor.lastName, COUNT(a) as aptCount FROM Appointment a GROUP BY a.doctor.lastName ORDER BY aptCount DESC")
    List<Object[]> countAppointmentsPerDoctor();

    @Query("SELECT a FROM Appointment a JOIN a.doctor d JOIN d.specializations s WHERE s.name = :specName")
    List<Appointment> findAppointmentsByDoctorSpecialization(@Param("specName") String specName);

    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);
}
