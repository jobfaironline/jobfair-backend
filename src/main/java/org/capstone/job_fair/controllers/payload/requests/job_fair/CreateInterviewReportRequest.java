package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInterviewReportRequest {
    private String applicationId;
    private String advantage;
    private String disadvantage;
    private String note;
}
