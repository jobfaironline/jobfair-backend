package org.capstone.job_fair.controllers.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.statuses.JobFairStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEvaluateJobFairRequest {
    @NotNull
    private String jobFairId;
    @NotNull
    private JobFairStatus status;
    @Size(min = DataConstraint.JobFair.MESSAGE_MIN_LENGTH, max = DataConstraint.JobFair.MESSAGE_MAX_LENGTH)
    private String message;
}
