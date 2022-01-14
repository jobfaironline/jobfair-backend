package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.WorkHistoryDTO;

public interface WorkHistoryService {
    void createWorkHistory(WorkHistoryDTO dto);
}
