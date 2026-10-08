package kigali.clinic.rw.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Set;

@Entity
@Table(name="sys_doctor")
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;
    
    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    @JsonIgnoreProperties("doctor")
    private Office office;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnoreProperties("doctor")
    private List<Appointment> appointments;

    @ManyToMany
    @JoinTable(
        name = "doctor_specialization",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    @JsonIgnoreProperties("doctors")
    private Set<Specialization> specializations;

    public Doctor() {}

    public Doctor(String firstName, String lastName, LocalDate dateOfBirth, Office office) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.office = office;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public Office getOffice() { return office; }
    public void setOffice(Office office) { this.office = office; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
    public Set<Specialization> getSpecializations() { return specializations; }
    public void setSpecializations(Set<Specialization> specializations) { this.specializations = specializations; }
}
