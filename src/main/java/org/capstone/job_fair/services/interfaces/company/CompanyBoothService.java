package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;

import java.util.List;
import java.util.Optional;

public interface CompanyBoothService {
    Optional<JobFairBoothDTO> getCompanyBoothByJobFairIdAndBoothId(String jobFairId, String boothId);

    List<JobFairBoothDTO> getCompanyBoothByJobFairIdAndCompanyId(String jobFairId, String companyId);

    Optional<JobFairBoothDTO> getById(String boothId);
}
