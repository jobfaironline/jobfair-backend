package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;

public interface JobPositionService {
    void createNewJobPosition(JobPositionDTO dto);

    JobPositionDTO updateJobPosition(JobPositionDTO dto, String companyId);

    void deleteJobPosition(String jobPositionId, String companyId);
}
