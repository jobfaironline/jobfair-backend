package org.capstone.job_fair.controllers.payload.requests.matching_point;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
public class CalculateMatchingPointApplicationRequest extends CalculateMatchingPointRequest{
    private String applicationId;
}
