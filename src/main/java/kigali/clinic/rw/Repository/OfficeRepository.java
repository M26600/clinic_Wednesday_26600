package kigali.clinic.rw.Repository;

import kigali.clinic.rw.domain.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {

    List<Office> findByBuildingName(String buildingName);

    List<Office> findByOfficeNumber(String officeNumber);

    List<Office> findByBuildingNameAndOfficeNumber(String buildingName, String officeNumber);

    List<Office> findByBuildingNameOrderByOfficeNumberAsc(String buildingName);

    List<Office> findByDoctorIsNull();

    List<Office> findByBuildingNameLike(String pattern);

    long countByBuildingName(String buildingName);

    @Query("SELECT o FROM Office o WHERE o.doctor IS NOT NULL")
    List<Office> findOccupiedOffices();

    @Query("SELECT o FROM Office o WHERE NOT EXISTS (SELECT d FROM Doctor d WHERE d.office = o) ORDER BY o.officeNumber ASC")
    List<Office> findFreeOffices();

    @Query("SELECT a.doctor.office.buildingName, a.doctor.office.officeNumber, COUNT(a) FROM Appointment a WHERE a.doctor.office IS NOT NULL GROUP BY a.doctor.office.buildingName, a.doctor.office.officeNumber ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice();
}
