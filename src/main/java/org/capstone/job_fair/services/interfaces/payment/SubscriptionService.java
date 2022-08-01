package org.capstone.job_fair.services.interfaces.payment;

import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    void chargeSubscription(String subscriptionPlanId, String companyId, CreditCardDTO creditCardDTO);

    List<SubscriptionPlanDTO> getAllSubscriptionPlans();

    Page<SubscriptionDTO> getAllSubscriptionOfCompany(String companyId, int offset, int pageSize, String sortBy, Sort.Direction direction);

    String getInvoiceUrlBySubscriptionOfCompany(String companyId, String subscriptionId);

    Optional<SubscriptionDTO> getCurrentSubscriptionOfCompany(String companyId);

    void cancelSubscriptionOfCompany(String companyId);
    Optional<SubscriptionPlanDTO> getSubscriptionPlanById(String id);
}
