package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;

public interface JobPositionService {
    void createNewJobPosition(JobPositionDTO dto);
}