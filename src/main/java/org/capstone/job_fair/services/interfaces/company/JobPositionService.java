package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.enums.JobLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface JobPositionService {
    void createNewJobPosition(JobPositionDTO dto);

    JobPositionDTO updateJobPosition(JobPositionDTO dto, String companyId);

    void deleteJobPosition(String jobPositionId, String companyId);

    Page<JobPositionDTO> getAllJobPositionOfCompany(String companyId, Integer jobEntityId, JobLevel jobLevelId, int pageSize, int offset, String sortBy, Sort.Direction direction);

}
