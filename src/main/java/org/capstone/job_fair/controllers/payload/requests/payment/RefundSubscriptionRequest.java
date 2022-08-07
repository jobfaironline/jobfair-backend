package org.capstone.job_fair.controllers.payload.requests.payment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundSubscriptionRequest {
    @NotNull
    private String subscriptionId;
    @NotNull
    @XSSConstraint
    private String reason;
    @NotNull
    private String adminEmail;
}
