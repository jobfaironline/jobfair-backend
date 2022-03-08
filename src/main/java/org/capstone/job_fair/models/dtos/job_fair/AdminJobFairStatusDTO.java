package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.JobFairAdminStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdminJobFairStatusDTO {
    private String jobFairId;
    private JobFairDTO jobFair;
    private JobFairAdminStatus status;
}
