package org.capstone.job_fair.models.dtos.payment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.entities.payment.SubscriptionPlanEntity;
import org.capstone.job_fair.models.statuses.SubscriptionRefundStatus;
import org.capstone.job_fair.models.statuses.SubscriptionStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubscriptionDTO {
    private String id;
    private SubscriptionStatus status;
    private Long currentPeriodStart;
    private Long currentPeriodEnd;
    private Long cancelAt;
    private Integer defaultPaymentMethod;
    private Double price;
    private SubscriptionRefundStatus refundStatus;
    private SubscriptionPlanDTO subscriptionPlan;
    @JsonIgnore
    private String transactionId;
    private String refundReason;
    private Integer jobfairQuota;
}
