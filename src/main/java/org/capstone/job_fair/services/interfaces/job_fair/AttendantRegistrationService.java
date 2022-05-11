package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.AttendantRegistrationDTO;
import org.springframework.data.domain.Page;

public interface AttendantRegistrationService {
    Page<AttendantRegistrationDTO> getAllRegisteredJobFair(int pageSize, int offset);

    AttendantRegistrationDTO visitJobFair(String userId, String jobFairId);

    AttendantRegistrationDTO registerJobFairAttendant(String userId, String jobFairId);
}
