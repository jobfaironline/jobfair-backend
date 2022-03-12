package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
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

    private Long companyRegisterStartTime;

    private Long companyRegisterEndTime;

    private Long companyBuyBoothStartTime;

    private Long companyBuyBoothEndTime;

    private Long attendantRegisterStartTime;

    private Long startTime;

    private Long endTime;

    private String description;

    private String layoutId;

    private JobFairPlanStatus status;

    private String creatorId;

    private String authorizerId;

    private String cancelReason;

    private String rejectReason;

    private String thumbnail;

    private String name;

    private Integer estimateParticipant;

    private String targetCompany;

    private String targetAttendant;

    private Long createTime;

    private Long updateTime;
}
