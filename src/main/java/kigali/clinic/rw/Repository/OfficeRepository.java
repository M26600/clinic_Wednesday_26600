package kigali.clinic.rw.Repository;

import kigali.clinic.rw.domain.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
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

    @org.springframework.data.jpa.repository.Query("SELECT o FROM Office o WHERE o.doctor IS NOT NULL")
    List<Office> findOccupiedOffices();
}
