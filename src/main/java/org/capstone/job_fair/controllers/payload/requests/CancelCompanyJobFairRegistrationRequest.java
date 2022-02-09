package org.capstone.job_fair.controllers.payload.requests;


import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CancelCompanyJobFairRegistrationRequest {
    @NotNull
    private String companyRegistrationId;

    @NotBlank
    @Size(max = DataConstraint.JobFair.COMPANY_REASON_CANCEL_JOB_FAIR_REGISTRATION_MAX_LENGTH)
    @XSSConstraint
    private String cancelReason;

}
