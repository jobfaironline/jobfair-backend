package org.capstone.job_fair.controllers.payment;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.payment.SubscriptionRequest;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.services.interfaces.payment.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;


    @PostMapping(ApiEndPoint.Subscription.SUBSCRIPTION_ENDPOINT)
    public ResponseEntity<?> testCard(@RequestBody @Valid SubscriptionRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setNumber(request.getCard().getNumber());
        creditCardDTO.setExp_month(request.getCard().getExp_month());
        creditCardDTO.setExp_year(request.getCard().getExp_year());
        creditCardDTO.setCvc(request.getCard().getCvc());
        subscriptionService.chargeSubscription(request.getSubscriptionId(), companyId, creditCardDTO);
        return ResponseEntity.ok().build();
    }
}
