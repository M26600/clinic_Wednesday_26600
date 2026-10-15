package kigali.clinic.rw.Repository;

import kigali.clinic.rw.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findBySsNumber(String ssNumber);
    boolean existsBySsNumber(String ssNumber);

    List<Patient> findByLastName(String lastName);

    List<Patient> findByPhone(String phone);

    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String first, String last);

    List<Patient> findByPhoneStartingWith(String prefix);

    List<Patient> findByAppointments_DateAfter(LocalDateTime date);

    long countByLastName(String lastName);

    // A1: Derived (IgnoreCase, OrderBy)
    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT p FROM Patient p WHERE p.appointments IS EMPTY")
    List<Patient> findPatientsWithNoAppointments();

    @Query("SELECT p FROM Patient p JOIN p.appointments a GROUP BY p.id HAVING COUNT(a) > :minAppointments")
    List<Patient> findPatientsWithMultipleAppointments(@Param("minAppointments") long minAppointments);

    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") UUID doctorId);

    // C2: Frequent patients
    @Query("SELECT p FROM Patient p JOIN p.appointments a GROUP BY p HAVING COUNT(a) >= :min ORDER BY COUNT(a) DESC")
    List<Patient> findFrequentPatients(@Param("min") long min);
}
