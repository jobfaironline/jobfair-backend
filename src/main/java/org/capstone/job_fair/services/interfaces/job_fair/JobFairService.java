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

    Page<JobFairDTO> getAllForAdmin(JobFairPlanStatus status, String jobFairName, int offset, int pageSize, String sortBy, Sort.Direction direction);

    List<JobFairDTO> getAllJobFairByStatus(JobFairPlanStatus jobFairPlanStatus);

    Optional<JobFairDTO> getJobFairByID(String id);

    List<JobFairDTO> getAllAvailableForRegistration(String fromTime, String toTime);

    Page<CompanyJobFairStatusDTO> getJobFairForCompany(String companyId, String jobFairName, List<JobFairCompanyStatus> status, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Optional<CompanyJobFairStatusDTO> getJobFairForCompanyByJobFairId(String companyId, String jobfairId);


    Optional<AttendantJobFairStatusDTO> getJobFairForAttendantByJobFairId(String attendantId, String jobfairId);

    Optional<AdminJobFairStatusDTO> getJobFairForAdminByJobFairId(String jobfairId);

    Page<AttendantJobFairStatusDTO> getJobFairForAttendant(String attendantId, String jobFairName, List<JobFairAttendantStatus> status, int offset, int pageSize, String sortBy, Sort.Direction direction);

    Page<AdminJobFairStatusDTO> getJobFairForAdmin(List<JobFairAdminStatus> statuses, String jobFairName, int offset, int pageSize, String sortBy, Sort.Direction direction);


}
