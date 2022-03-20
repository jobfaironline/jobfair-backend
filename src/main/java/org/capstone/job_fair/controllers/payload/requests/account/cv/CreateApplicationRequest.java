package org.capstone.job_fair.controllers.payload.requests.account.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CreateApplicationRequest {
    @XSSConstraint
    @NotEmpty
    @Size(max = DataConstraint.Application.SUMMARY_MAX_LENGTH)
    private String summary;
    @XSSConstraint
    @NotEmpty
    private String registrationJobPositionId;
    @NotEmpty
    private String cvId;
}
