package kigali.clinic.rw.domain;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.Set;

@Entity
@Table(name="sys_specialization")
public class Specialization {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "specializations")
    private Set<Doctor> doctors;

    public Specialization() {}

    public Specialization(String name) {
        this.name = name;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<Doctor> getDoctors() { return doctors; }
    public void setDoctors(Set<Doctor> doctors) { this.doctors = doctors; }
}
