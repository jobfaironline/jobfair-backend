package org.capstone.job_fair.controllers.attendant.cv;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.account.cv.CreateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.UpdateAttendantRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.enums.Application;
import org.capstone.job_fair.services.interfaces.attendant.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.NoSuchElementException;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

//    @Autowired
//    private ApplicationMapper applicationMapper;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Application.APPLICATION_ENDPOINT)
    public ResponseEntity create(@Validated @RequestBody CreateApplicationRequest request) {
        try{
            //get accountId from Jwt
            SecurityContext securityContext = SecurityContextHolder.getContext();
            UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
            String accountId = user.getId();
            //call applicationDTO and accountDTO
            ApplicationDTO dto = new ApplicationDTO();
            AccountDTO accountDTO = new AccountDTO();
            //
            accountDTO.setId(accountId);
            AttendantDTO attendantDTO = new AttendantDTO();
            attendantDTO.setAccount(accountDTO);
            RegistrationJobPositionDTO regisDTO = new RegistrationJobPositionDTO();
            regisDTO.setId(request.getRegistrationJobPositionId());
            //
            dto.setSummary(request.getSummary());
            dto.setCreateDate(new Date().getTime());
            dto.setStatus(Application.DRAFT);
            dto.setAttendantDTO(attendantDTO);
            dto.setRegistrationJobPositionDTO(regisDTO);
            ApplicationDTO result = applicationService.createNewApplication(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        catch (NoSuchElementException | IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
