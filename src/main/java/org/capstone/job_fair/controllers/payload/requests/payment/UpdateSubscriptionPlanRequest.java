package org.capstone.job_fair.controllers.payload.requests.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateSubscriptionPlanRequest {
    @NotBlank
    private String id;
    private String name;
    private String description;
    private Double price;
    private Long validPeriod;
    private Integer jobfairQuota;
}
