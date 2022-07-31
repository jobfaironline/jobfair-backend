package org.capstone.job_fair.models.dtos.payment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubscriptionDTO {
    private String id;
    private Integer status;
    private Long currentPeriodStart;
    private Long currentPeriodEnd;
    private Long cancelAt;
    private Integer defaultPaymentMethod;
    @JsonIgnore
    private String transactionId;
}
