package org.capstone.job_fair.controllers.payload.requests;


import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CompanyJobFairRegistrationRequest {
    @NotNull
    private String jobFairId;


    @XSSConstraint
    @NotBlank
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

        @XSSConstraint
        @NotBlank
        @Size(max = DataConstraint.Company.MAX_DESCRIPTION_LENGTH)
        private String description;

        @XSSConstraint
        @NotBlank
        @Size(max = DataConstraint.Company.MAX_DESCRIPTION_LENGTH)
        private String requirements;

        @NotNull
        @NumberFormat
        @Min(DataConstraint.JobPosition.SALARY_MIN)
        private double minSalary;

        @NotNull
        @NumberFormat
        @Min(DataConstraint.JobPosition.SALARY_MIN)
        @Max(DataConstraint.JobPosition.SALARY_MAX)
        private double maxSalary;

        @NotNull
        @NumberFormat
        @Min(DataConstraint.JobPosition.EMPLOYEE_MIN)
        @Max(DataConstraint.JobPosition.EMPLOYEE_MAX)
        private int numOfPosition;

    }
}
