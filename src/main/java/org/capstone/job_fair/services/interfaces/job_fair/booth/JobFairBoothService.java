package org.capstone.job_fair.services.interfaces.job_fair.booth;

import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;

import java.util.List;
import java.util.Optional;

public interface JobFairBoothService {
    Optional<JobFairBoothDTO> getCompanyBoothByJobFairIdAndBoothId(String jobFairId, String boothId);

    List<JobFairBoothDTO> getCompanyBoothByJobFairId(String jobFairId);

    Optional<JobFairBoothDTO> getById(String boothId);

    Integer getBoothCountByJobFair(String jobFairId);

    JobFairBoothDTO updateJobFairBooth(JobFairBoothDTO jobFairBooth, String companyId);

}
