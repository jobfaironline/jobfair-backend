package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.statuses.JobFairStatus;

import java.util.List;

public interface JobFairService {
    void createJobFair(JobFairDTO dto);
    List<JobFairDTO> getAllJobFairPlanOfCurrentAccount();

    void deleteJobFairDraft(String jobFairId);

    void submitJobFairDraft(String jobFairId);

    void cancelPendingJobFair(String jobFairId);

    void restoreDeletedJobFair(String jobFairId);

    void adminEvaluateJobFair(String jobFairId, JobFairStatus status, String message);

    List<JobFairDTO> getAll();
}
