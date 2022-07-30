package org.capstone.job_fair.services.impl.payment;

import com.stripe.exception.StripeException;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.payment.SubscriptionEntity;
import org.capstone.job_fair.models.entities.payment.SubscriptionPlanEntity;
import org.capstone.job_fair.repositories.payment.SubscriptionPlanRepository;
import org.capstone.job_fair.repositories.payment.SubscriptionRepository;
import org.capstone.job_fair.services.interfaces.payment.StripeService;
import org.capstone.job_fair.services.interfaces.payment.SubscriptionService;
import org.capstone.job_fair.services.mappers.payment.SubscriptionPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private StripeService stripeService;
    @Autowired
    private SubscriptionPlanMapper subscriptionPlanMapper;

    @Override
    public void chargeSubscription(String subscriptionPlanId, String companyId, CreditCardDTO creditCardDTO) {
        //Check if this company already has a subscription, if there is, contact support for help.
        Long currentDate = new Date().getTime();
        Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findCurrentSubscriptionByCompanyId(companyId, currentDate);
        if (subscriptionEntityOptional.isPresent()) {
            throw new IllegalArgumentException("Subscription already exists");
        }
        //If company has no subscription, check for subscriptionPlanId, if it doesn't exist, throw exception.
        Optional<SubscriptionPlanEntity> subscriptionPlanOptional = subscriptionPlanRepository.findById(subscriptionPlanId);
        if (!subscriptionPlanOptional.isPresent()) {
            throw new IllegalArgumentException("SubscriptionPlan does not exist");
        }
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanOptional.get();
        //Call Stripe service to create card token
        String token = null;
        try {
            token = stripeService.createChargeToken(creditCardDTO);
        } catch (StripeException e) {
            e.printStackTrace();
        }
        //Call Stripe service to create charge
        String chargeId = null;
        try {
            String price = Integer.toString((int)Math.round(subscriptionPlanEntity.getPrice().doubleValue()));
            System.out.println("Price: " + price);
            chargeId = stripeService.createCharge( price, "usd", companyId, creditCardDTO, token);
            //After charge is created, create subscription
            SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
            CompanyEntity companyEntity = new CompanyEntity();
            companyEntity.setId(companyId);
            subscriptionEntity.setCompany(companyEntity);
            subscriptionEntity.setSubscriptionPlanEntity(subscriptionPlanEntity);
            subscriptionEntity.setTransactionId(chargeId);
            long ONE_YEAR_IN_MILLIS = 31556952000L;
            subscriptionEntity.setCurrentPeriodStart(currentDate);
            subscriptionEntity.setCurrentPeriodEnd(currentDate + ONE_YEAR_IN_MILLIS);
            subscriptionEntity.setPrice(subscriptionPlanEntity.getPrice());
            subscriptionRepository.save(subscriptionEntity);
        } catch (StripeException e) {
            e.printStackTrace();
        }

    }

    public List<SubscriptionPlanDTO> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll().stream().map(subscriptionPlanMapper::toDTO).collect(java.util.stream.Collectors.toList());
    }

}
