package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;

import java.util.Optional;

public interface CompanyBoothService {
    Optional<CompanyBoothDTO> getCompanyBoothByJobFairIdAndBoothId(String jobFairId, String boothId);
}
