package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.enums.AssignmentType;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignEmployeeRequest {
    @NotNull
    private String employeeId;
    @NotNull
    private String jobFairBoothId;
    @NotNull
    private AssignmentType type;
    private Long beginTime;
    private Long endTime;
}
