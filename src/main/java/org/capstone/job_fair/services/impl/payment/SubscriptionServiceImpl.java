package org.capstone.job_fair.services.impl.payment;

import com.stripe.exception.StripeException;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.payment.SubscriptionEntity;
import org.capstone.job_fair.models.entities.payment.SubscriptionPlanEntity;
import org.capstone.job_fair.models.statuses.SubscriptionRefundStatus;
import org.capstone.job_fair.models.statuses.SubscriptionStatus;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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
    @Transactional
    public SubscriptionDTO chargeSubscription(String subscriptionPlanId, String companyId, CreditCardDTO creditCardDTO) {
        //Check if this company already has a subscription, if there is, contact support for help.
        Long currentDate = new Date().getTime();
//        Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findByCompanyIdAndCurrentPeriodStartAfterAndCurrentPeriodEndBeforeAndStatusNotOrStatusNull(companyId, currentDate, SubscriptionStatus.CANCELED);
//        if (subscriptionEntityOptional.isPresent()) {
//            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.ALREADY_EXISTS));
//        }
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
            String price = Integer.toString((int) Math.round(subscriptionPlanEntity.getPrice().doubleValue() * 100));
            Optional<CompanyEntity> companyEntityOptional = companyRepository.findById(companyId);
            String description = " " + subscriptionPlanEntity.getName() + " plan of company " + companyEntityOptional.get().getName();
            chargeId = stripeService.createCharge(price, "usd", description, creditCardDTO, token);
            //After charge is created, create subscription
            SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
            CompanyEntity companyEntity = companyRepository.findById(companyId).get();
            subscriptionEntity.setStatus(SubscriptionStatus.ACTIVE);
            subscriptionEntity.setJobfairQuota(subscriptionPlanEntity.getJobfairQuota());
            subscriptionEntity.setMaxJobFairQuota(subscriptionPlanEntity.getJobfairQuota());
            subscriptionEntity.setCompany(companyEntity);
            subscriptionEntity.setSubscriptionPlan(subscriptionPlanEntity);
            subscriptionEntity.setTransactionId(chargeId);
            subscriptionEntity.setCurrentPeriodStart(currentDate);
            long ONE_MONTH_IN_MILLIS = 2629800000L ;
            long END_DATE = subscriptionPlanEntity.getValidPeriod() * ONE_MONTH_IN_MILLIS;
            subscriptionEntity.setCurrentPeriodEnd(currentDate + END_DATE);
            subscriptionEntity.setPrice(subscriptionPlanEntity.getPrice());
            subscriptionEntity = subscriptionRepository.save(subscriptionEntity);
            return subscriptionMapper.toDTO(subscriptionEntity);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Payment.PAYMENT_ERROR));
        }
    }

    @Override
    public Page<SubscriptionPlanDTO> getAllSubscriptionPlans(int offset, int pageSize, String sortBy, Sort.Direction direction, String name) {
        return subscriptionPlanRepository.findAllByNameContains(name, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy))).map(subscriptionPlanMapper::toDTO);
    }

    @Override
    public Page<SubscriptionDTO> getAllSubscription(int offset, int pageSize, String sortBy, Sort.Direction direction, String companyName) {
        return subscriptionRepository.findAllByCompanyNameContains(companyName, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy))).map(subscriptionMapper::toDTO);
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
        } catch (StripeException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Payment.GET_INVOICE_ERROR));
        }
    }


    @Override
    @Transactional
    public void refundSubscriptionOfCompany(String companyId, String subscriptionId, String reason) {
        Long currentDate = new Date().getTime();
        Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findByCompanyIdAndId(companyId, subscriptionId);
        if (!subscriptionEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.NOT_FOUND));
        }
        SubscriptionEntity subscriptionEntity = subscriptionEntityOptional.get();
        //If subscription expired, throw exception.
        if (subscriptionEntity.getCurrentPeriodEnd() < currentDate) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.EXPIRED));
        }
        //If subscription is inactive, throw exception.
        if (subscriptionEntity.getStatus() == SubscriptionStatus.INACTIVE) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.INACTIVE));
        }
        //If quota is used, throw exception.
        if (subscriptionEntity.getJobfairQuota() == 0) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.JOBFAIR_QUOTA_EXCEEDED));
        }
        //If subscription still in available time and has not been used, set refund status to REQUESTED_REFUND and inactivate this subscription.
        if (subscriptionEntity.getStatus() == SubscriptionStatus.ACTIVE) {
            subscriptionEntity.setStatus(SubscriptionStatus.INACTIVE);
            subscriptionEntity.setRefundStatus(SubscriptionRefundStatus.REQUESTED_REFUND);
            subscriptionEntity.setRefundReason(reason);
        }
        subscriptionRepository.save(subscriptionEntity);
    }

    @Override
    @Transactional
    public void evaluateRefundRequest(String subscriptionId, SubscriptionRefundStatus status) {
        Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.NOT_FOUND));
        SubscriptionEntity subscriptionEntity = subscriptionEntityOptional.get();
        //Check if subscription is in REQUESTED_REFUND status
        if (subscriptionEntity.getRefundStatus() != SubscriptionRefundStatus.REQUESTED_REFUND)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.NOT_REQUESTED_REFUND));
        //If status is DECLINED, make this subscription active again.
        if (status == SubscriptionRefundStatus.REFUND_DECLINED) {
            subscriptionEntity.setStatus(SubscriptionStatus.ACTIVE);
            subscriptionEntity.setRefundStatus(SubscriptionRefundStatus.REFUND_DECLINED);
        }
        //If status is APPROVED, make this subscription inactive and set cancel date to current date.
        if (status == SubscriptionRefundStatus.REFUNDED) {
            subscriptionEntity.setStatus(SubscriptionStatus.INACTIVE);
            subscriptionEntity.setRefundStatus(SubscriptionRefundStatus.REFUNDED);
            subscriptionEntity.setCancelAt(new Date().getTime());
        }
        subscriptionRepository.save(subscriptionEntity);
    }

    @Override
    public Optional<SubscriptionDTO> getSubscriptionById(String id) {
        return subscriptionRepository.findById(id).map(subscriptionMapper::toDTO);
    }


    @Override
    public Optional<SubscriptionPlanDTO> getSubscriptionPlanById(String id) {
        Optional<SubscriptionPlanEntity> opt = subscriptionPlanRepository.findById(id);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SubscriptionPlan.NOT_FOUND));
        }
        return Optional.of(subscriptionPlanMapper.toDTO(opt.get()));
    }

    @Override
    @Transactional
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) {
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanMapper.toEntity(subscriptionPlanDTO);
        subscriptionPlanRepository.save(subscriptionPlanEntity);
        return subscriptionPlanMapper.toDTO(subscriptionPlanEntity);
    }

    @Override
    @Transactional
    public SubscriptionPlanDTO updateSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) {
        Optional<SubscriptionPlanEntity> subscriptionPlanEntityOptional = subscriptionPlanRepository.findById(subscriptionPlanDTO.getId());
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanEntityOptional.get();
        subscriptionPlanMapper.UpdateSubscriptionPlanFromDTO(subscriptionPlanDTO, subscriptionPlanEntity);
        subscriptionPlanRepository.save(subscriptionPlanEntity);
        return subscriptionPlanMapper.toDTO(subscriptionPlanEntity);
    }

    @Override
    @Transactional
    public SubscriptionPlanDTO deleteSubscriptionPlan(String id) {
        Optional<SubscriptionPlanEntity> subscriptionPlanEntityOptional = subscriptionPlanRepository.findById(id);
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanEntityOptional.get();
        subscriptionPlanRepository.delete(subscriptionPlanEntity);
        return subscriptionPlanMapper.toDTO(subscriptionPlanEntity);
    }

    public List<SubscriptionDTO> getAllSubscription() {
        return subscriptionRepository.findAll().stream().map(subscriptionMapper::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public void decreaseJobFairQuota(String subscriptionId){
        //Get subscription by id
        Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionEntityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.NOT_FOUND));
        }
        SubscriptionEntity subscriptionEntity = subscriptionEntityOptional.get();
        if(subscriptionEntity.getJobfairQuota() > 0 ) {
            subscriptionEntity.setJobfairQuota(subscriptionEntity.getJobfairQuota() - 1);
        } else {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Subscription.JOBFAIR_QUOTA_EXCEEDED));
        }
        subscriptionRepository.save(subscriptionEntity);
    }


}
