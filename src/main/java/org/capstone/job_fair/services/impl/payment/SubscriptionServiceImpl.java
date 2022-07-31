package org.capstone.job_fair.services.impl.payment;

import com.stripe.exception.StripeException;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.payment.SubscriptionEntity;
import org.capstone.job_fair.models.entities.payment.SubscriptionPlanEntity;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.repositories.payment.SubscriptionPlanRepository;
import org.capstone.job_fair.repositories.payment.SubscriptionRepository;
import org.capstone.job_fair.services.interfaces.payment.StripeService;
import org.capstone.job_fair.services.interfaces.payment.SubscriptionService;
import org.capstone.job_fair.services.mappers.payment.SubscriptionMapper;
import org.capstone.job_fair.services.mappers.payment.SubscriptionPlanMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void chargeSubscription(String subscriptionPlanId, String companyId, CreditCardDTO creditCardDTO) {
        //Check if this company already has a subscription, if there is, contact support for help.
        Long currentDate = new Date().getTime();
        Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findCurrentSubscriptionByCompanyId(companyId, currentDate);
        if (subscriptionEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.ALREADY_EXISTS));
        }
        //If company has no subscription, check for subscriptionPlanId, if it doesn't exist, throw exception.
        Optional<SubscriptionPlanEntity> subscriptionPlanOptional = subscriptionPlanRepository.findById(subscriptionPlanId);
        if (!subscriptionPlanOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SubscriptionPlan.NOT_FOUND));
        }
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanOptional.get();
        //Call Stripe service to create card token
        String token = null;
        try {
            token = stripeService.createChargeToken(creditCardDTO);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Payment.CARD_ERROR));
        }
        //Call Stripe service to create charge
        String chargeId = null;
        try {
            String price = Integer.toString((int)Math.round(subscriptionPlanEntity.getPrice().doubleValue()*100));
            Optional<CompanyEntity> companyEntityOptional = companyRepository.findById(companyId);
            String description = " " + subscriptionPlanEntity.getName() + " plan of company " + companyEntityOptional.get().getName();
            chargeId = stripeService.createCharge( price, "usd", description, creditCardDTO, token);
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
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Payment.PAYMENT_ERROR));
        }

    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll().stream().map(subscriptionPlanMapper::toDTO).collect(java.util.stream.Collectors.toList());
    }
    @Override
    public Page<SubscriptionDTO> getAllSubscriptionOfCompany(String companyId, int offset, int pageSize, String sortBy, Sort.Direction direction) {
        return subscriptionRepository.findAllByCompanyId(companyId, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy))).map(subscriptionMapper::toDTO);
    }

    @Override
    public String getInvoiceUrlBySubscriptionOfCompany(String companyId, String subscriptionId) {
        Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findByCompanyIdAndId(companyId, subscriptionId);
        if (!subscriptionEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.NOT_FOUND));
        }
        SubscriptionEntity subscriptionEntity = subscriptionEntityOptional.get();
        try {
            return stripeService.getReceipt(subscriptionEntity.getTransactionId());
        } catch (StripeException e){
            e.printStackTrace();
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Payment.GET_INVOICE_ERROR));
        }
    }




}