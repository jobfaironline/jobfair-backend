package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.statuses.JobFairAdminStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFairForAdminResponse {
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

    private JobFairAdminStatus status;

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
