package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.enums.Application;
import org.springframework.data.domain.Page;

public interface ApplicationService {
    ApplicationDTO createNewApplication(ApplicationDTO dto);

    Page<ApplicationDTO> getAllApplicationsOfAttendantByCriteria(String attendantId, Application status, long fromTime, long toTime, int offset, int pageSize, String field);

}
