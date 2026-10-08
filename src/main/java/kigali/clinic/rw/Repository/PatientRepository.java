package kigali.clinic.rw.Repository;

import kigali.clinic.rw.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    List<Patient> findByNamesContaining(String names);
    List<Patient> findByPhone(String phone);
    List<Patient> findByNamesContainingIgnoreCase(String names);
    
    List<Patient> findByPhoneStartingWith(String prefix);
    List<Patient> findByAppointments_DateAfter(java.time.LocalDateTime date);
    long countByNamesContainingIgnoreCase(String nameFragment);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Patient p WHERE p.appointments IS EMPTY")
    List<Patient> findPatientsWithNoAppointments();
    
    @org.springframework.data.jpa.repository.Query("SELECT p FROM Patient p JOIN p.appointments a GROUP BY p.id HAVING COUNT(a) > :minAppointments")
    List<Patient> findPatientsWithMultipleAppointments(@org.springframework.data.repository.query.Param("minAppointments") long minAppointments);
}
