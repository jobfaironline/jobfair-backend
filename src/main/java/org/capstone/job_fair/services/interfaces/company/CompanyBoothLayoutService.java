package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutVideoDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CompanyBoothLayoutService {
    List<CompanyBoothLayoutDTO> getLayoutsByCompanyBoothId(String companyBoothId);

    Optional<CompanyBoothLayoutDTO> getLatestVersionByCompanyBoothId(String companyBoothId);

    Optional<CompanyBoothLayoutDTO> getById(String id);

    CompanyBoothLayoutDTO createNew(CompanyBoothLayoutDTO dto, MultipartFile file);

    CompanyBoothLayoutVideoDTO createNewVideoWithFile(CompanyBoothLayoutVideoDTO dto);

    CompanyBoothLayoutVideoDTO createNewVideoWithUrl(CompanyBoothLayoutVideoDTO dto);
}
