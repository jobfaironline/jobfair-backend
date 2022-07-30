package org.capstone.job_fair.services.interfaces.payment;

import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;

public interface SubscriptionService {
    void chargeSubscription(String subscriptionPlanId, String companyId, CreditCardDTO creditCardDTO);
    }
