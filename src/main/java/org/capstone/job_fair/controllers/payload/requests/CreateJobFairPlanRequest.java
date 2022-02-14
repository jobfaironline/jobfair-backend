package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateJobFairPlanRequest {

    @NotNull
    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private long companyRegisterStartTime;

    @NotNull
    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private long companyRegisterEndTime;

    @NotNull
    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private long companyBuyBoothStartTime;

    @NotNull
    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private long companyBuyBoothEndTime;

    @NotNull
    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private long attendantRegisterStartTime;

    @NotNull
    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long startTime;

    @NotNull
    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private long endTime;

    @XSSConstraint
    @NotNull
    @NotBlank
    @Size(max = DataConstraint.JobFair.MAX_DESCRIPTION_LENGTH)
    private String description;

    @NotNull

    private String layoutId;


}
