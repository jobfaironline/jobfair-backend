package org.capstone.job_fair.controllers.payload.requests.company;


import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateCompanyJobFairRegistrationRequest {

    @NotNull
    private String jobFairRegistrationId;


    @XSSConstraint
    @Size(max = DataConstraint.Company.MAX_DESCRIPTION_LENGTH)
    private String description;

    @Valid
    @Size(min = DataConstraint.Company.MIN_JOB_POSITION)
    private List<JobPosition> jobPositions;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobPosition {

        @NotNull
        private String jobPositionId;

        @NumberFormat
        @Min(DataConstraint.JobPosition.SALARY_MIN)
        private Double minSalary;

        @NumberFormat
        @Min(DataConstraint.JobPosition.SALARY_MIN)
        @Max(DataConstraint.JobPosition.SALARY_MAX)
        private Double maxSalary;

        @NumberFormat
        @Min(DataConstraint.JobPosition.EMPLOYEE_MIN)
        @Max(DataConstraint.JobPosition.EMPLOYEE_MAX)
        private Integer numOfPosition;

    }
}
