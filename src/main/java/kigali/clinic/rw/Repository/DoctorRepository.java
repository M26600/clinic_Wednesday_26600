package kigali.clinic.rw.Repository;

import kigali.clinic.rw.domain.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    List<Doctor> findByLastName(String lastName);
    List<Doctor> findByFirstNameAndLastName(String firstName, String lastName);
    List<Doctor> findByOffice_OfficeNumber(String officeNumber);

    Page<Doctor> findByLastName(String lastName, Pageable pageable);
    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String first, String last);
    List<Doctor> findBySpecializations_Name(String specName);
    long countByOffice_BuildingName(String buildingName);
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE s.name = :specName")
    List<Doctor> findBySpecializationName(@Param("specName") String specName);

    // @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE s.name = :name")
    // List<Doctor> getDocsBySpec(String name);
    
    @Query(value = "SELECT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name)")
    List<Doctor> findBySpecializationNameIgnoreCaseJoin(@Param("name") String name);

    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName ASC")
    List<Doctor> findDoctorsWithoutOffice();
}
