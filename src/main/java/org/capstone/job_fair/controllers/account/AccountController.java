package org.capstone.job_fair.controllers.account;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.ChangePasswordRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.token.AccountVerifyTokenDTO;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.token.AccountVerifyTokenService;
import org.capstone.job_fair.services.mappers.token.AccountVerifyTokenMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountVerifyTokenService accountVerifyTokenService;

    @Autowired
    private AccountVerifyTokenMapper mapper;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Account.ACCOUNT_ENDPOINT)
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Account.ACCOUNT_ENDPOINT + "/{id}")
    public ResponseEntity<?> getAttendant(@PathVariable("id") String id) {
        Optional<AccountDTO> opt = accountService.getAccountById(id);
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt);
        }
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = ApiEndPoint.Authorization.VERIFY_USER + "/{userId}/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable("userId") String userId, @PathVariable("token") String tokenId) {
        AccountVerifyTokenDTO token = accountVerifyTokenService.getLastedToken(userId);
        if (token == null) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.INVALID_VERIFY_TOKEN), HttpStatus.BAD_REQUEST
            );
        }
        if (token.getIsInvalidated()) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.INVALIDATED_VERIFY_TOKEN), HttpStatus.BAD_REQUEST
            );
        }
        accountVerifyTokenService.invalidateEntity(mapper.toAccountVerifyTokenEntity(token));
        if (token.getExpiredTime() < new Date().getTime()) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.EXPIRED_TOKEN),
                    HttpStatus.BAD_REQUEST);
        }
        accountService.activateAccount(userId);

        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.AccessControlMessage.VERIFY_ACCOUNT_SUCCESSFULLY),
                HttpStatus.OK);
    }

    @PostMapping(path = ApiEndPoint.Account.CHANGE_PASSWORD_ENDPOINT)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            accountService.changePassword(request.getNewPassword(), request.getOldPassword());
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.CHANGE_PASSWORD_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = ApiEndPoint.Authorization.NEW_VERIFY_LINK + "/{id}")
    public ResponseEntity<?> getNewVerifyLink(@PathVariable String id) {
        try {
            accountService.sendVerifyAccountEmail(id);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Account.SEND_NEW_VERIFICATION_LINK_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
