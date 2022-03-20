package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationForCompanyResponse {
    String id;
    String candidateName;
    Long appliedDate;
    String jobPositionTitle;
    String jobFairName;
    String jobFairId;
    String jobPositionId;
}
