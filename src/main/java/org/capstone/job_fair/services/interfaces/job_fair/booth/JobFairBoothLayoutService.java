package org.capstone.job_fair.services.interfaces.job_fair.booth;

import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutVideoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface JobFairBoothLayoutService {
    List<JobFairBoothLayoutDTO> getLayoutsByCompanyBoothId(String companyBoothId);

    Optional<JobFairBoothLayoutDTO> getLatestVersionByCompanyBoothId(String companyBoothId);

    Optional<JobFairBoothLayoutDTO> getById(String id);

    JobFairBoothLayoutDTO createNew(JobFairBoothLayoutDTO dto, MultipartFile file);

    JobFairBoothLayoutVideoDTO createNewVideoWithFile(JobFairBoothLayoutVideoDTO dto);

    JobFairBoothLayoutVideoDTO createNewVideoWithUrl(JobFairBoothLayoutVideoDTO dto);
}
