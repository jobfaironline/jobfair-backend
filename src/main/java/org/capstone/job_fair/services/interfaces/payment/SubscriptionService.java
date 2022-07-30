package org.capstone.job_fair.services.interfaces.payment;

import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;

import java.util.List;

public interface SubscriptionService {
    void chargeSubscription(String subscriptionPlanId, String companyId, CreditCardDTO creditCardDTO);
    List<SubscriptionPlanDTO> getAllSubscriptionPlans();
    }
