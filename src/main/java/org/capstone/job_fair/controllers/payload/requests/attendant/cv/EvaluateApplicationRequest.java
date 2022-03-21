package org.capstone.job_fair.controllers.payload.requests.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class EvaluateApplicationRequest {
    @NotNull
    public ApplicationStatus status;
    @XSSConstraint
    @Size(min = DataConstraint.Application.EVALUATE_MESSAGE_MIN_LENGTH, max = DataConstraint.Application.EVALUATE_MESSAGE_MAX_LENGTH)
    public String evaluateMessage;
}
