package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JobFairDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private Long createTime;
    private Long decorateStartTime;
    private Long decorateEndTime;
    private Long publicStartTime;
    private Long publicEndTime;
    private String name;
    private String description;
    private String targetAttendant;
    private String thumbnailUrl;
    private JobFairPlanStatus status;
    private String cancelReason;
    private String hostName;
    private CompanyDTO company;
}
