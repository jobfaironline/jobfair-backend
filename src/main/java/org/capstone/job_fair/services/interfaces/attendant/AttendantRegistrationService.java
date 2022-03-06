package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.AttendantRegistrationDTO;
import org.springframework.data.domain.Page;

public interface AttendantRegistrationService {
    Page<AttendantRegistrationDTO> getAllRegisteredJobFair(int pageSize, int offset);
}
