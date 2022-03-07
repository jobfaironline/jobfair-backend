package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.JobFairAttendantStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AttendantJobFairStatusDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String attendantId;
    private JobFairDTO jobFair;
    private JobFairAttendantStatus status;
}
