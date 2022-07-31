package org.capstone.job_fair.services.interfaces.payment;

import com.stripe.exception.StripeException;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;

public interface StripeService {
    String createCharge(String amount, String currency, String description, CreditCardDTO creditCardDTO, String token) throws StripeException;
    String createChargeToken(CreditCardDTO creditCardDTO) throws StripeException;

    String getReceipt(String chargeId) throws StripeException;

    }
