package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.JobFairCompanyStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyJobFairStatusDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String companyId;
    private JobFairDTO jobFair;
    private JobFairCompanyStatus status;
}
