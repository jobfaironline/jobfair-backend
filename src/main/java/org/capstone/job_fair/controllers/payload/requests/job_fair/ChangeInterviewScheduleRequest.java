package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeInterviewScheduleRequest {
    @NotNull
    private String applicationId;
    @NotNull
    private Long beginTime;
    @NotNull
    private Long endTime;
    private String requestMessage;
}
