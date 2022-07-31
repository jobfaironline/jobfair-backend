package org.capstone.job_fair.controllers.payment;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.SubscriptionConstant;
import org.capstone.job_fair.controllers.payload.requests.payment.SubscriptionRequest;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.capstone.job_fair.services.interfaces.payment.SubscriptionService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.Subscription.PAY_SUBSCRIPTION)
    public ResponseEntity<?> purchaseSubscription(@RequestBody @Valid SubscriptionRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setNumber(request.getCard().getNumber());
        creditCardDTO.setExp_month(request.getCard().getExp_month());
        creditCardDTO.setExp_year(request.getCard().getExp_year());
        creditCardDTO.setCvc(request.getCard().getCvc());
        subscriptionService.chargeSubscription(request.getSubscriptionId(), companyId, creditCardDTO);
        return ResponseEntity.ok(MessageUtil.getMessage(MessageConstant.Subscription.CREATED));
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Subscription.SUBSCRIPTION_ENDPOINT)
    public ResponseEntity<?> getAllSubscriptionPlan() {
        List<SubscriptionPlanDTO> subscriptionPlanDTOList = subscriptionService.getAllSubscriptionPlans();
        if (subscriptionPlanDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptionPlanDTOList);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Subscription.COMPANY_SUBSCRIPTION)
    public ResponseEntity<?> getAllSubscriptionOfCompany(@RequestParam(value = "offset", defaultValue = SubscriptionConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                                         @RequestParam(value = "pageSize", defaultValue = SubscriptionConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                                         @RequestParam(value = "sortBy", defaultValue = SubscriptionConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                                         @RequestParam(value = "direction", required = false, defaultValue = SubscriptionConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        Page<SubscriptionDTO> subscriptionDTOList = subscriptionService.getAllSubscriptionOfCompany(companyId, offset, pageSize, sortBy, direction);
        if (subscriptionDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptionDTOList);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Subscription.GET_INVOICE_OF_SUBSCRIPTION + "/{subscriptionId}")
    public ResponseEntity<?> getInvoiceUrlBySubscriptionOfCompany(@PathVariable(value = "subscriptionId") String subscriptionId){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        String invoiceUrl = subscriptionService.getInvoiceUrlBySubscriptionOfCompany(companyId, subscriptionId);
        if (invoiceUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoiceUrl);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Subscription.CURRENT_SUBSCRIPTION_OF_COMPANY)
    public ResponseEntity<?> getCurrentSubscriptionOfCompany(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        Optional<SubscriptionDTO> subscriptionDTOOptional = subscriptionService.getCurrentSubscriptionOfCompany(companyId);
        if(!subscriptionDTOOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(subscriptionDTOOptional.get());
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Subscription.CANCEL_SUBSCRIPTION_OF_COMPANY)
    public ResponseEntity<?> cancelSubscriptionOfCompany(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        subscriptionService.cancelSubscriptionOfCompany(companyId);
        return ResponseEntity.ok(MessageUtil.getMessage(MessageConstant.Subscription.CANCELED));
    }




}
