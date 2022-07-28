package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.constants.JobFairConstant;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairProgressDTO;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JobFairService {
    Optional<JobFairDTO> getById(String id);

    JobFairDTO createNewJobFair(JobFairDTO dto);

    JobFairDTO updateJobFair(JobFairDTO dto, String companyID);

    JobFairDTO deleteJobFair(String jobFairId, String companyID);

    Page<JobFairDTO> findByNameAndCompanyIdAndStatus(String name, String companyId, JobFairPlanStatus status, Pageable pageable);

    void publishJobFair(String companyId, String jobFairId);

    JobFairDTO validateJobFairForPublish(String companyId, String jobFairId);

    JobFairDTO createOrUpdateJobFairThumbnail(String jobfairThumbnailFolder, String jobFairId, String companyId);

    Page<JobFairDTO> findJobFairForAttendantByCriteria(String name, String countryId, String subCategoryId, Pageable pageable);

    JobFairProgressDTO getJobFairProgress(String jobFairId);

    Page<JobFairDTO> findJobFairForAdmin(String name, JobFairConstant.AdminSearchStatus status, Pageable pageable);
}
