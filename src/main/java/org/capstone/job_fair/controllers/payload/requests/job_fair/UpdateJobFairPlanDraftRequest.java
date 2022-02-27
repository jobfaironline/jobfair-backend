package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateJobFairPlanDraftRequest {

    @NotBlank
    private String jobFairID;

    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long companyRegisterStartTime;

    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long companyRegisterEndTime;

    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long companyBuyBoothStartTime;

    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long companyBuyBoothEndTime;

    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long attendantRegisterStartTime;

    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long startTime;

    @NumberFormat
    @Min(DataConstraint.JobFair.MIN_TIME)
    private Long endTime;

    @XSSConstraint
    @Size(max = DataConstraint.JobFair.MAX_DESCRIPTION_LENGTH)
    private String description;

    private String layoutId;

}
