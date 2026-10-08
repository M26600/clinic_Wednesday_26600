package kigali.clinic.rw.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="sys_office")
public class Office {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String officeNumber;
    private String buildingName;

    @OneToOne(mappedBy = "office")
    private Doctor doctor;

    public Office() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) { this.officeNumber = officeNumber; }
    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
}
