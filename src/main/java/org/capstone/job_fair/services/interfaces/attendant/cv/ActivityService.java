package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.ActivityDTO;

public interface ActivityService {
    void createNewActivity(ActivityDTO dto);
}
