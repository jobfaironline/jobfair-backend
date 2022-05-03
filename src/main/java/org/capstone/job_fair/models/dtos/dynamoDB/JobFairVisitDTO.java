package org.capstone.job_fair.models.dtos.dynamoDB;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JobFairVisitDTO {
    private String userId;
    private String jobFairId;
    private String jobFairBoothId;
}
