package org.capstone.job_fair.services.interfaces.payment;

import org.capstone.job_fair.controllers.payload.responses.SubscriptionReceiptResponse;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.capstone.job_fair.models.statuses.SubscriptionRefundStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface SubscriptionService {
    SubscriptionDTO chargeSubscription(String subscriptionPlanId, String companyId, CreditCardDTO creditCardDTO);

    Page<SubscriptionPlanDTO> getAllSubscriptionPlans(int offset, int pageSize, String sortBy, Sort.Direction direction, String name);
    Page<SubscriptionDTO> getAllSubscription(int offset, int pageSize, String sortBy, Sort.Direction direction, String companyName);

    Page<SubscriptionDTO> getAllSubscriptionOfCompany(String companyId, int offset, int pageSize, String sortBy, Sort.Direction direction);

    String getInvoiceUrlBySubscriptionOfCompany(String companyId, String subscriptionId);

    void refundSubscriptionOfCompany(String companyId, String subscriptionId, String reason, String accountId);
    Optional<SubscriptionPlanDTO> getSubscriptionPlanById(String id);
    SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);
    SubscriptionPlanDTO updateSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);
    SubscriptionPlanDTO deleteSubscriptionPlan(String id);
    void evaluateRefundRequest(String subscriptionId, SubscriptionRefundStatus status);

    Optional<SubscriptionDTO> getSubscriptionById(String id);

    void decreaseJobFairQuota(String subscriptionId);

    SubscriptionReceiptResponse getReceiptData(String subscriptionId,  String companyId);
}
