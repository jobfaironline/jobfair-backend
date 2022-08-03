package org.capstone.job_fair.controllers.payload.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubscriptionReceiptResponse {
    private Long amount;
    private Long purchaseDate;
    private String currency;
    private String paymentMethod;
    private String description;
    private String last4;
}
