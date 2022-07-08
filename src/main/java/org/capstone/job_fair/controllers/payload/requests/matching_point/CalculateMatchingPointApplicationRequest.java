package org.capstone.job_fair.controllers.payload.requests.matching_point;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CalculateMatchingPointApplicationRequest extends CalculateMatchingPointRequest{
    private String applicationId;
}
