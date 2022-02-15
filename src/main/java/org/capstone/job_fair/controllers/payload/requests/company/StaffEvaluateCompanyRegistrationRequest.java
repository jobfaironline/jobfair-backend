package org.capstone.job_fair.controllers.payload.requests.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffEvaluateCompanyRegistrationRequest {
    @NotNull
    private String companyRegistrationId;
    @NotNull
    private CompanyRegistrationStatus status;
    @Size(min = DataConstraint.CompanyRegistration.MESSAGE_MIN_LENGTH, max = DataConstraint.CompanyRegistration.MESSAGE_MAX_LENGTH)
    private String message;
}