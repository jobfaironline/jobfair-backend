package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.enums.ApplicationStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationForAttendantResponse {
    private String id;

    private String createDate;
    private String summary;

    private ApplicationStatus status;
    private String evaluateMessage;
    private Long evaluateDate;
    private String authorizerName;

    private String jobFairName;
    private String jobFairId;

    private String jobPositionTitle;
    private String jobPositionId;
    private String boothName;
    private String companyName;
}
