package org.capstone.job_fair.controllers.payload.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.capstone.job_fair.models.statuses.SubscriptionRefundStatus;
import org.capstone.job_fair.models.statuses.SubscriptionStatus;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubscriptionResponse {
    private String id;
    private SubscriptionStatus status;
    private Long currentPeriodStart;
    private Long currentPeriodEnd;
    private Long cancelAt;
    private Integer defaultPaymentMethod;
    private Double price;
    private SubscriptionRefundStatus refundStatus;
    private SubscriptionPlanDTO subscriptionPlan;
    private String refundReason;
    private Integer jobfairQuota;
    private Integer maxJobFairQuota;
    private String companyName;
}
