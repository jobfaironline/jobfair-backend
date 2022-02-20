package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;

import java.util.List;
import java.util.Optional;

public interface CompanyBoothLayoutService {
    List<CompanyBoothLayoutDTO> getLayoutsByCompanyBoothId(String companyBoothId);

    Optional<CompanyBoothLayoutDTO> getLatestVersionByCompanyBoothId(String companyBoothId);

    Optional<CompanyBoothLayoutDTO> getById(String id);

    CompanyBoothLayoutDTO createNew(CompanyBoothLayoutDTO dto);
}
