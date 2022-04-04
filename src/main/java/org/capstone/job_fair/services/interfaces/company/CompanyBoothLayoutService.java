package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutVideoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CompanyBoothLayoutService {
    List<JobFairBoothLayoutDTO> getLayoutsByCompanyBoothId(String companyBoothId);

    Optional<JobFairBoothLayoutDTO> getLatestVersionByCompanyBoothId(String companyBoothId);

    Optional<JobFairBoothLayoutDTO> getById(String id);

    JobFairBoothLayoutDTO createNew(JobFairBoothLayoutDTO dto, MultipartFile file);

    JobFairBoothLayoutVideoDTO createNewVideoWithFile(JobFairBoothLayoutVideoDTO dto);

    JobFairBoothLayoutVideoDTO createNewVideoWithUrl(JobFairBoothLayoutVideoDTO dto);
}
