package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CompanyBuyBoothRequest;
import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothPurchaseService;
import org.capstone.job_fair.services.interfaces.company.CompanyRegistrationService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class CompanyBuyBoothController {

    @Autowired
    private CompanyRegistrationService companyRegistrationService;

    @Autowired
    private CompanyBoothPurchaseService companyBoothPurchaseService;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.BoothPurchase.BOOTH_PURCHASE)
    public ResponseEntity<?> companyBuyBooth(@Valid @RequestBody CompanyBuyBoothRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CompanyRegistrationDTO> companyRegistrationDTOOpt = companyRegistrationService.getById(request.getCompanyRegistrationId());
        if (!companyRegistrationDTOOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyRegistration.NOT_FOUND));
        }
        CompanyRegistrationDTO companyRegistrationDTO = companyRegistrationDTOOpt.get();
        if (!companyRegistrationDTO.getCompanyId().equals(userDetails.getCompanyId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.COMPANY_MISSMATCH));
        }
        CompanyBoothDTO companyBoothDTO = companyBoothPurchaseService.purchaseBooth(request.getCompanyRegistrationId(), request.getBoothId());

        return ResponseEntity.ok(companyBoothDTO);
    }
}
