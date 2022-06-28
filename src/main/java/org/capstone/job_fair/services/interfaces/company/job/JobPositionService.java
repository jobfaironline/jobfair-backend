package org.capstone.job_fair.services.interfaces.company.job;

import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
import org.capstone.job_fair.models.enums.JobLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface JobPositionService {
    JobPositionDTO createNewJobPosition(JobPositionDTO dto);

    JobPositionDTO updateJobPosition(JobPositionDTO dto, String companyId);

    void deleteJobPosition(String jobPositionId, String companyId);

    Page<JobPositionDTO> getAllJobPositionOfCompany(String companyId, Integer jobTypeId, JobLevel jobLevelId, String jobTitle, int pageSize, int offset, String sortBy, Sort.Direction direction);

    ParseFileResult<JobPositionDTO> createNewJobPositionsFromFile(MultipartFile file, String companyId);

    Optional<JobPositionDTO> getByIdAndCompanyId(String id, String companyId);

}
