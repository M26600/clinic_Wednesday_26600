package kigali.clinic.rw.Repository;

import kigali.clinic.rw.domain.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, UUID> {
    List<Specialization> findByNameContaining(String name);
    List<Specialization> findByNameIgnoreCase(String name);
    List<Specialization> findByName(String name);
    
    List<Specialization> findByNameStartingWithIgnoreCase(String prefix);
    List<Specialization> findByDoctors_LastName(String lastName);
    long countByName(String name);

    @org.springframework.data.jpa.repository.Query("SELECT s FROM Specialization s JOIN s.doctors d WHERE d.id = :doctorId")
    List<Specialization> findByDoctorId(@org.springframework.data.repository.query.Param("doctorId") UUID doctorId);

    // @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM specialization WHERE doctors IS NULL", nativeQuery = true)
    // List<Specialization> findUnusedSpec();

    @org.springframework.data.jpa.repository.Query("SELECT s FROM Specialization s WHERE s.doctors IS EMPTY")
    List<Specialization> findUnusedSpecializations();
}
