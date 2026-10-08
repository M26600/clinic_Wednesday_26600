package kigali.clinic.rw.controller;

import kigali.clinic.rw.service.OfficeService;
import kigali.clinic.rw.domain.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offices")
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
        Office existingOffice = officeService.getOfficeById(id);
        if (existingOffice != null) {
            existingOffice.setOfficeNumber(officeDetails.getOfficeNumber());
            existingOffice.setBuildingName(officeDetails.getBuildingName());
            return officeService.saveOffice(existingOffice);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteOffice(@PathVariable UUID id) {
        officeService.deleteOffice(id);
    }
}