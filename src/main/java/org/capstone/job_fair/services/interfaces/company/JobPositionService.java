package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;

public interface JobPositionService {
    void createNewJobPosition(JobPositionDTO dto);

}
