package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;

public interface ApplicationService {
    ApplicationDTO createNewApplication (ApplicationDTO dto);
}
