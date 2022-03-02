package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.JobFairCompanyStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyJobFairDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String description;
    private JobFairCompanyStatus status;
    private String message;
}
