package kigali.clinic.rw.domain;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sys_patient")
public class Patient{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String ssNumber;
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;
    public Patient(){}
    public Patient(String firstName, String lastName, String phone, String ssNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.ssNumber = ssNumber;
    }
    public Patient(String firstName, String lastName, String phone, List<Appointment> appointments){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.appointments = appointments;
    }
    public UUID getId(){
        return id;
    }
    public void setId(UUID id){
        this.id = id;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return phone;
    }
    public String getSsNumber(){
        return ssNumber;
    }
    public void setSsNumber(String ssNumber){
        this.ssNumber = ssNumber;
    }
    public List<Appointment> getAppointments(){
        return appointments;
    }
    public void setAppointments(List<Appointment> appointments){
        this.appointments = appointments;
    }

}