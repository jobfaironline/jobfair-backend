package org.capstone.job_fair.controllers.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.CompanyRegistrationConstant;
import org.capstone.job_fair.models.dtos.attendant.AttendantRegistrationDTO;
import org.capstone.job_fair.services.interfaces.attendant.AttendantRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendantRegistrationController {

    @Autowired
    AttendantRegistrationService attendantRegistrationService;

    @GetMapping(ApiEndPoint.AttendantRegistration.REGISTERED_JOB_FAIR)
    public ResponseEntity<?> getAllRegisteredJobFairOfAttendant(@RequestParam(value = "offset", defaultValue = CompanyRegistrationConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset, @RequestParam(value = "pageSize", defaultValue = CompanyRegistrationConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize) {
        Page<AttendantRegistrationDTO> attendantRegistrationDTOPage = attendantRegistrationService.getAllRegisteredJobFair(pageSize, offset);
        if (attendantRegistrationDTOPage.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(attendantRegistrationDTOPage);
    }
}
