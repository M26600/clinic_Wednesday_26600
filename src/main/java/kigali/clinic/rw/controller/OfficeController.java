package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;

@RestController
@RequestMapping("/api/offices")
@CrossOrigin(origins = "http://localhost:5173")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @GetMapping
    public List<Office> getAllOffices() {
        return officeService.getAllOffices();
    }

    @PostMapping
    public Office createOffice(@RequestBody Office office) {
        return officeService.saveOffice(office);
    }

    @GetMapping("/{id}")
    public Office getOfficeById(@PathVariable UUID id) {
        return officeService.getOfficeById(id);
    }

    @PutMapping("/{id}")
    public Office updateOffice(@PathVariable UUID id, @RequestBody Office officeDetails) {
        return officeService.updateOffice(id, officeDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteOffice(@PathVariable UUID id) {
        officeService.deleteOffice(id);
    }

    @GetMapping("/free")
    public List<Office> getFreeOffices() {
        return officeService.getFreeOffices();
    }

    @GetMapping("/busiest")
    public Object getBusiestOffice() {
        return officeService.getBusiestOffice();
    }
}