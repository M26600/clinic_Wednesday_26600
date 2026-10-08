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
    private String names;
    private String phone;
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;
    public Patient(){}
    public Patient(String names, String phone, List<Appointment> appointments){
        this.names = names;
        this.phone = phone;
        this.appointments = appointments;
    }
    public UUID getId(){
        return id;
    }
    public void setId(UUID id){
        this.id = id;
    }
    public String getNames(){
        return names;
    }

    public void setNames(String names){
        this.names = names;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return phone;
    }
    public List<Appointment> getAppointments(){
        return appointments;
    }
    public void setAppointments(List<Appointment> appointments){
        this.appointments = appointments;
    }

}