package org.capstone.job_fair.services.impl.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.services.interfaces.payment.StripeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeServiceImpl implements StripeService {

    private String STRIPE_API_PUBLIC_KEY = "pk_test_51LQ5yrGVSxTgdaBL1Xzk8F1GZXD09ebBVpQ2OShzSPIeQXnOIUSpQT7R1TgyV6sxkbE7VUJ2JsweXuE00yGsz68t00MFjKCEzA";
    private String STRIPE_API_SECRET_KEY = "sk_test_51LQ5yrGVSxTgdaBLx5SecTytaLEDfHTZCox4LBpBdF7My5kEKBAqQuzBWUIDyHcAMKCdmy2G36pZgy08iRG5thlZ00Nh5zOprd";


    @Override
    public String createChargeToken(CreditCardDTO creditCardDTO) throws StripeException {
        Stripe.apiKey = STRIPE_API_SECRET_KEY;
        Map<String, Object> card = new HashMap<>();
        card.put("number", creditCardDTO.getNumber());
        card.put("exp_month", creditCardDTO.getExp_month());
        card.put("exp_year", creditCardDTO.getExp_year());
        card.put("cvc", creditCardDTO.getCvc());
        Map<String, Object> params = new HashMap<>();
        params.put("card", card);

        Token token = Token.create(params);
        return token.getId();
    }

    @Override
    public String createCharge(String amount, String currency, String description, CreditCardDTO creditCardDTO, String token) throws StripeException {
        String id = null;
        Stripe.apiKey = STRIPE_API_SECRET_KEY;
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for " + description);
        chargeParams.put("source", token);
        //create a charge
        Charge charge = Charge.create(chargeParams);
        id = charge.getId();
        return id;
    }

    @Override
    public String getReceipt(String chargeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_SECRET_KEY;
        Charge charge = Charge.retrieve(chargeId);
        return charge.getReceiptUrl();
    }

    public Charge getChargeObject(String chargeId) throws StripeException {
        Stripe.apiKey = STRIPE_API_SECRET_KEY;
        Charge charge = Charge.retrieve(chargeId);
        return charge;
    }

}
