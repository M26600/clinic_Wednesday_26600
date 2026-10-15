package kigali.clinic.rw.service;

import kigali.clinic.rw.Repository.OfficeRepository;
import kigali.clinic.rw.domain.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public Office getOfficeById(UUID id) {
        return officeRepository.findById(id).orElse(null);
    }

    public Office saveOffice(Office office) {
        return officeRepository.save(office);
    }

    public Office updateOffice(UUID id, Office officeDetails) {
        Office office = officeRepository.findById(id).orElse(null);
        if (office != null) {
            office.setOfficeNumber(officeDetails.getOfficeNumber());
            office.setBuildingName(officeDetails.getBuildingName());
            return officeRepository.save(office);
        }
        return null;
    }

    public void deleteOffice(UUID id) {
        officeRepository.deleteById(id);
    }

    public List<Office> getFreeOffices() {
        return officeRepository.findFreeOffices();
    }

    public Object getBusiestOffice() {
        List<Object[]> list = officeRepository.findBusiestOffice();
        if (list.isEmpty()) {
            return "No appointments yet";
        }
        return list.get(0);
    }
}