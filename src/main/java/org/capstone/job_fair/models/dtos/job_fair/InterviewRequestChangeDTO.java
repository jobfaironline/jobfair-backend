package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.enums.InterviewRequestChangeStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InterviewRequestChangeDTO {
    private String id;
    private String message;
    private Long beginTime;
    private Long endTime;
    private ApplicationDTO application;
    private InterviewRequestChangeStatus status;
    private Long createTime;
    private Long evaluateTime;

}
