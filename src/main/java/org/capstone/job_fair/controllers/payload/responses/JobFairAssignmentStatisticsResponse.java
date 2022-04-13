package org.capstone.job_fair.controllers.payload.responses;

import lombok.*;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class JobFairAssignmentStatisticsResponse {
    private int boothTotal;
    private int assignedBoothNum;
    private int employeeTotal;
    private int assignedEmployeeNum;
}
