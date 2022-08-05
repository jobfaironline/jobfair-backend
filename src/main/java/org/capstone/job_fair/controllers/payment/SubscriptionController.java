package org.capstone.job_fair.controllers.payment;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.SubscriptionConstant;
import org.capstone.job_fair.constants.SubscriptionPlanConstant;
import org.capstone.job_fair.controllers.payload.requests.payment.CreateSubscriptionPlanRequest;
import org.capstone.job_fair.controllers.payload.requests.payment.RefundSubscriptionRequest;
import org.capstone.job_fair.controllers.payload.requests.payment.SubscriptionRequest;
import org.capstone.job_fair.controllers.payload.requests.payment.UpdateSubscriptionPlanRequest;
import org.capstone.job_fair.controllers.payload.responses.SubscriptionReceiptResponse;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.payment.CreditCardDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionDTO;
import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.statuses.SubscriptionRefundStatus;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.interfaces.payment.SubscriptionService;
import org.capstone.job_fair.services.mappers.payment.SubscriptionMapper;
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
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;


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
        SubscriptionDTO result = subscriptionService.chargeSubscription(request.getSubscriptionId(), companyId, creditCardDTO);
        return ResponseEntity.ok(subscriptionMapper.toResponse(result));
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.SubscriptionPlan.SUBSCRIPTION_PLAN_ENDPOINT)
    public ResponseEntity<?> getAllSubscriptionPlan(@RequestParam(value = "offset", defaultValue = SubscriptionPlanConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                                    @RequestParam(value = "pageSize", defaultValue = SubscriptionPlanConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = SubscriptionPlanConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                                    @RequestParam(value = "direction", required = false, defaultValue = SubscriptionPlanConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
                                                    @RequestParam(value = "name", required = false, defaultValue = "") String name) {
        Page<SubscriptionPlanDTO> subscriptionPlanDTOList = subscriptionService.getAllSubscriptionPlans(offset, pageSize, sortBy, direction, name);
        if (subscriptionPlanDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptionPlanDTOList);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Subscription.ADMIN_GET_SUBSCRIPTION_BY_CRITERIA)
    public ResponseEntity<?> getAllSubscription(@RequestParam(value = "offset", defaultValue = SubscriptionConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                                    @RequestParam(value = "pageSize", defaultValue = SubscriptionConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = SubscriptionConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                                    @RequestParam(value = "direction", required = false, defaultValue = SubscriptionConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
                                                    @RequestParam(value = "companyName", required = false, defaultValue = "") String companyName) {
        Page<SubscriptionDTO> subscriptionPlanDTOList = subscriptionService.getAllSubscription(offset, pageSize, sortBy, direction, companyName);
        if (subscriptionPlanDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptionPlanDTOList.map(subscriptionMapper::toResponse));
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.SubscriptionPlan.SUBSCRIPTION_PLAN_ENDPOINT + "/{id}")
    public ResponseEntity<?> getSubscriptionPlanById(@PathVariable("id") String id) {
        Optional<SubscriptionPlanDTO> opt = subscriptionService.getSubscriptionPlanById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(opt.get());
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
        return ResponseEntity.ok(subscriptionDTOList.map(subscriptionMapper::toResponse));
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Subscription.GET_INVOICE_OF_SUBSCRIPTION + "/{subscriptionId}")
    public ResponseEntity<?> getInvoiceUrlBySubscriptionOfCompany(@PathVariable(value = "subscriptionId") String subscriptionId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        String invoiceUrl = subscriptionService.getInvoiceUrlBySubscriptionOfCompany(companyId, subscriptionId);
        if (invoiceUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoiceUrl);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.Subscription.CANCEL_SUBSCRIPTION_OF_COMPANY)
    public ResponseEntity<?> cancelSubscriptionOfCompany(@RequestBody @Valid RefundSubscriptionRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        String requesterId = userDetails.getId();
        subscriptionService.refundSubscriptionOfCompany(companyId, request.getSubscriptionId(), request.getReason(), requesterId);

        //send notification to admin
        NotificationMessageDTO message = NotificationMessageDTO.builder()
                .message(MessageUtil.getMessage(MessageConstant.NotificationMessage.REQUEST_TO_REFUND_SUBSCRIPTION.MESSAGE))
                .title(MessageUtil.getMessage(MessageConstant.NotificationMessage.REQUEST_TO_REFUND_SUBSCRIPTION.TITLE))
                .notificationType(NotificationType.NOTI)
                .build();
        //find accountId by adminEmail
        Optional<AccountEntity> opt = accountService.getActiveAccountByEmail(request.getAdminEmail());
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        String accountId = opt.get().getId();

        notificationService.createNotification(message, accountId);
        return ResponseEntity.ok(MessageUtil.getMessage(MessageConstant.Subscription.REQUESTED_REFUND));
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @PostMapping(ApiEndPoint.SubscriptionPlan.SUBSCRIPTION_PLAN_ENDPOINT)
    public ResponseEntity<?> createSubscriptionPlan(@RequestBody @Valid CreateSubscriptionPlanRequest request) {
        SubscriptionPlanDTO subscriptionPlanDTO = new SubscriptionPlanDTO();
        subscriptionPlanDTO.setName(request.getName());
        subscriptionPlanDTO.setDescription(request.getDescription());
        subscriptionPlanDTO.setPrice(request.getPrice());
        subscriptionPlanDTO.setValidPeriod(request.getValidPeriod());
        subscriptionPlanDTO.setJobfairQuota(request.getJobfairQuota());
        subscriptionPlanDTO = subscriptionService.createSubscriptionPlan(subscriptionPlanDTO);
        return ResponseEntity.ok(subscriptionPlanDTO);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @PutMapping(ApiEndPoint.SubscriptionPlan.SUBSCRIPTION_PLAN_ENDPOINT)
    public ResponseEntity<?> updateSubscriptionPlan(@RequestBody @Valid UpdateSubscriptionPlanRequest request) {
        SubscriptionPlanDTO subscriptionPlanDTO = new SubscriptionPlanDTO();
        subscriptionPlanDTO.setId(request.getId());
        subscriptionPlanDTO.setName(request.getName());
        subscriptionPlanDTO.setDescription(request.getDescription());
        subscriptionPlanDTO.setPrice(request.getPrice());
        subscriptionPlanDTO.setValidPeriod(request.getValidPeriod());
        subscriptionPlanDTO.setJobfairQuota(request.getJobfairQuota());
        subscriptionPlanDTO = subscriptionService.updateSubscriptionPlan(subscriptionPlanDTO);
        return ResponseEntity.ok(subscriptionPlanDTO);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @DeleteMapping(ApiEndPoint.SubscriptionPlan.SUBSCRIPTION_PLAN_ENDPOINT + "/{id}")
    public ResponseEntity<?> deleteSubscriptionPlan(@PathVariable(value = "id") String id) {
        SubscriptionPlanDTO dto = subscriptionService.deleteSubscriptionPlan(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Subscription.EVALUATE_REFUND_REQUEST)
    public ResponseEntity<?> evaluateSubscriptionRefundRequest(@RequestParam(value = "subscriptionId") String subscriptionId,
                                                               @RequestParam(value = "status")SubscriptionRefundStatus status) {
        subscriptionService.evaluateRefundRequest(subscriptionId, status);

        //send notification to company manager
        NotificationMessageDTO message = null;
        if (status == SubscriptionRefundStatus.REFUNDED) {
            //approved
            message = NotificationMessageDTO.builder()
                    .message(MessageUtil.getMessage(MessageConstant.NotificationMessage.EVALUATE_REQUEST_TO_REFUND_SUBSCRIPTION.APPROVE_MESSAGE))
                    .title(MessageUtil.getMessage(MessageConstant.NotificationMessage.EVALUATE_REQUEST_TO_REFUND_SUBSCRIPTION.TITLE))
                    .notificationType(NotificationType.NOTI)
                    .build();
        } else {
            //reject
            message = NotificationMessageDTO.builder()
                    .message(MessageUtil.getMessage(MessageConstant.NotificationMessage.EVALUATE_REQUEST_TO_REFUND_SUBSCRIPTION.REJECT_MESSAGE))
                    .title(MessageUtil.getMessage(MessageConstant.NotificationMessage.EVALUATE_REQUEST_TO_REFUND_SUBSCRIPTION.TITLE))
                    .notificationType(NotificationType.NOTI)
                    .build();
        }

        Optional<SubscriptionDTO> subscriptionDTO = subscriptionService.getSubscriptionById(subscriptionId);
        if (!subscriptionDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println(subscriptionDTO.get());
        String accountId = subscriptionDTO.get().getAccount().getId();
        notificationService.createNotification(message, accountId);
        return ResponseEntity.ok(MessageUtil.getMessage(MessageConstant.Subscription.REFUND_REQUEST_EVALUATED));
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Subscription.SUBSCRIPTION_ENDPOINT + "/{subscriptionId}")
    public ResponseEntity<?> getSubscriptionById(@PathVariable("subscriptionId") String id) {
        Optional<SubscriptionDTO> opt = subscriptionService.getSubscriptionById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptionMapper.toResponse(opt.get()));
    }

    @GetMapping(ApiEndPoint.Subscription.SUBSCRIPTION_ENDPOINT + "/quota/"+"/{subscriptionId}")
    public ResponseEntity<?> decreaseJobFairQuota(@PathVariable(value = "subscriptionId") String subscriptionId) {
        subscriptionService.decreaseJobFairQuota(subscriptionId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Subscription.GET_INVOICE_OF_SUBSCRIPTION + "/data/{subscriptionId}")
    public ResponseEntity<?> getReceiptInfo(@PathVariable(value = "subscriptionId") String subscriptionId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        SubscriptionReceiptResponse response = subscriptionService.getReceiptData(subscriptionId, companyId);
        return ResponseEntity.ok(response);
    }

}
