package org.capstone.job_fair.controllers.account;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.StaffRegisterRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
public class StaffController {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping(ApiEndPoint.Staff.STAFF_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> register(@RequestBody @Valid StaffRegisterRequest request) {
        try {
            AccountDTO accountDTO = accountMapper.toDTO(request);
            accountDTO.setRole(Role.STAFF);
            accountDTO.setStatus(AccountStatus.REGISTERED);
            String password = PasswordGenerator.generatePassword();
            accountDTO.setPassword(encoder.encode(password));
            accountDTO.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
            accountService.createNew(accountDTO);

            this.mailService.sendMail(request.getEmail(),
                            MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_SUBJECT),
                            MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_CONTENT) + password)
                    .exceptionally(throwable -> {
                        log.error(throwable.getMessage());
                        return null;
                    });

            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Staff.CREATE_SUCCESSFULLY), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
