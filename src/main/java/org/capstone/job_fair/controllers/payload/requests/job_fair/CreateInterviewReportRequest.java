package org.capstone.job_fair.controllers.payload.requests.job_fair;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private boolean isQualified;
}
