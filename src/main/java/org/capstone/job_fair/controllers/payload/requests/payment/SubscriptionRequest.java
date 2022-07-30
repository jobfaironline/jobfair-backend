package org.capstone.job_fair.controllers.payload.requests.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest implements Serializable {
    @NotNull
    private String subscriptionId;
    @Valid
    private Card card;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Card {
        @NotNull
        private String number;
        @NotNull
        private String exp_month;
        @NotNull
        private String exp_year;
        @NotNull
        private String cvc;
    }



}
