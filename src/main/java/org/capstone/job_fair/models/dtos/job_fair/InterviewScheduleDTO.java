package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.statuses.InterviewStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InterviewScheduleDTO {
    @EqualsAndHashCode.Include
    private String id;
    private Long endTime;
    private Long beginTime;
    private String name;
    private String description;
    private InterviewStatus status;
    private String interviewerId;
    private String attendantId;
    private Long jobFairPublicEndTime;
    private String attendantName;
    private String interviewRoomId;
    private String waitingRoomId;
    private String interviewerName;
}
