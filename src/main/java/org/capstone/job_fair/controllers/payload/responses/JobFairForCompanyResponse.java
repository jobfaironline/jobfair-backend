package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.statuses.JobFairCompanyStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFairForCompanyResponse {
    private String id;
    private String description;
    private JobFairCompanyStatus status;
}
