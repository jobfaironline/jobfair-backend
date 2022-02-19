package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelJobFairRequest {
    @NotNull
    private String id;
    @NotNull
    @Size(min = DataConstraint.JobFair.MESSAGE_MIN_LENGTH, max = DataConstraint.JobFair.MESSAGE_MAX_LENGTH)
    private String message;
}
