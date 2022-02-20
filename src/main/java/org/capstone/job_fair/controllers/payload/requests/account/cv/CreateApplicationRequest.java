package org.capstone.job_fair.controllers.payload.requests.account.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CreateApplicationRequest {
    @XSSConstraint
    @NotEmpty
    private String summary;
    @XSSConstraint
    @NotEmpty
    private String registrationJobPositionId;
}
