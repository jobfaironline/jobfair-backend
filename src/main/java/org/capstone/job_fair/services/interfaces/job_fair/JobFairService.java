package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.AdminJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.AttendantJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.CompanyJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.statuses.JobFairAdminStatus;
import org.capstone.job_fair.models.statuses.JobFairAttendantStatus;
import org.capstone.job_fair.models.statuses.JobFairCompanyStatus;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface JobFairService {
    void draftJobFair(JobFairDTO dto);

    Page<JobFairDTO> getJobFairPlanByCreatorIdAndStatus(String creatorId, JobFairPlanStatus status, int offset, int pageSize, String sortBy, Sort.Direction direction);

    void deleteJobFairDraft(String jobFairId);

    void submitJobFairDraft(String jobFairId);

    void updateJobFairDraft(JobFairDTO dto);

    void cancelPendingJobFair(String jobFairId, String reason);

    void restoreDeletedJobFair(String jobFairId);

    void adminEvaluateJobFair(String jobFairId, JobFairPlanStatus status, String message);

    Page<JobFairDTO> getAllForAdmin(JobFairPlanStatus status, int offset, int pageSize, String sortBy, Sort.Direction direction);

    List<JobFairDTO> getAllJobFairByStatus(JobFairPlanStatus jobFairPlanStatus);

    Optional<JobFairDTO> getJobFairByID(String id);

    List<JobFairDTO> getAllAvailableForRegistration(String fromTime, String toTime);

    Page<CompanyJobFairStatusDTO> getJobFairForCompany(String companyId, JobFairCompanyStatus status, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Page<AttendantJobFairStatusDTO> getJobFairForAttendant(String attendantId, JobFairAttendantStatus status, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Page<AdminJobFairStatusDTO> getJobFairForAdmin(List<JobFairAdminStatus> statuses, int offset, int pageSize, String sortBy, Sort.Direction direction);


}
