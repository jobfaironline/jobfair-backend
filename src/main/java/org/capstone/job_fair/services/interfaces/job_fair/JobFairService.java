package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JobFairService {
    Optional<JobFairDTO> getById(String id);

    JobFairDTO createNewJobFair(JobFairDTO dto);

    JobFairDTO updateJobFair(JobFairDTO dto, String companyID);

    JobFairDTO deleteJobFair(String jobFairId, String companyID);

    Page<JobFairDTO> findByNameAndCompanyId(String name, String companyId, Pageable pageable);

}
