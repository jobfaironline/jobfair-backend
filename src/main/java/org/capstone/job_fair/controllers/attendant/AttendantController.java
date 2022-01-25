package org.capstone.job_fair.controllers.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.interfaces.attendant.CountryService;
import org.capstone.job_fair.services.interfaces.attendant.ResidenceService;
import org.capstone.job_fair.services.interfaces.token.AccountVerifyTokenService;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.services.mappers.AttendantMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class AttendantController {

    @Autowired
    private AttendantMapper attendantMapper;

    @Autowired
    private AttendantService attendantService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ResidenceService residenceService;

    @Autowired
    private AccountVerifyTokenService accountVerifyTokenService;

    @Autowired
    private MailService mailService;

    @Value("${api.endpoint}")
    String domain;



    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT)
    public ResponseEntity<List<AttendantDTO>> getAllAccounts() {
        return new ResponseEntity<>(attendantService.getAllAttendants(), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PutMapping(ApiEndPoint.Attendant.UPDATE_ENDPOINT)
    public ResponseEntity<?> update(@Validated @RequestBody UpdateAttendantRequest request) {
        try {
            AttendantDTO attendantDTO = attendantMapper.toDTO(request);
            attendantService.updateAccount(attendantDTO);
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Attendant.UPDATE_PROFILE_SUCCESSFULLY),
                    HttpStatus.OK);
        } catch (NoSuchElementException | IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(ApiEndPoint.Attendant.REGISTER_ENDPOINT)
    @Async("threadPoolTaskExecutor")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterAttendantRequest req) {


        try {
            AttendantDTO attendantDTO = attendantMapper.toDTO(req);
            attendantDTO = attendantService.createNewAccount(attendantDTO);
            String id = accountVerifyTokenService.createToken(attendantDTO.getAccount().getId()).getAccountId();

            this.mailService.sendMail(req.getEmail(),
                    MessageUtil.getMessage(MessageConstant.Attendant.ACCOUNT_VERIFY_MAIL_TITLE),
                    domain + ApiEndPoint.Authorization.VERIFY_USER+"/"+attendantDTO.getAccount().getId()+"/"+id);
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Attendant.REGISTER_SUCCESSFULLY),
                    HttpStatus.CREATED);
        } catch (NoSuchElementException | IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT + "/{id}")
    public ResponseEntity<?> getAttendant(@PathVariable("id") String id) {
        Optional<AttendantDTO> opt = attendantService.getAttendantById(id);
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

}
