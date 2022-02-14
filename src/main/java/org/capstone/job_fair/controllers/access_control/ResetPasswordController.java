package org.capstone.job_fair.controllers.access_control;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.ResetPasswordTokenConstants;
import org.capstone.job_fair.controllers.payload.requests.accesss_control.GenerateOTPRequest;
import org.capstone.job_fair.controllers.payload.requests.account.ResetPasswordRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.token.PasswordResetTokenEntity;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.token.PasswordResetTokenService;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

@RestController
@Slf4j
public class ResetPasswordController {

    @Value("${reset-password-expiration}")
    private String RESET_PASSWORD_TOKEN_EXPIRED_TIME;

    @Autowired
    private PasswordResetTokenService resetService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @PostMapping(ApiEndPoint.Authentication.RESET_PASSWORD_ENDPOINT)
    @Transactional
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        //check matching password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.CONFIRM_PASSWORD_MISMATCH),
                    HttpStatus.BAD_REQUEST);
        }
        //check existed account
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(request.getEmail());
        if (!accountOpt.isPresent()) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND),
                    HttpStatus.BAD_REQUEST);
        }
        AccountEntity account = accountOpt.get();
        //check existed token
        Optional<PasswordResetTokenEntity> tokenOpt = resetService.findTokenByOTPAndAccountID(request.getOtp(), account.getId());
        if (!tokenOpt.isPresent()) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.INVALID_OTP),
                    HttpStatus.BAD_REQUEST);
        }
        PasswordResetTokenEntity token = tokenOpt.get();
        //check if token account is the same as request account
        if (!account.equals(token.getAccount())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.INVALID_OTP),
                    HttpStatus.BAD_REQUEST);
        }
        //check if token is invalidated
        if (token.isInvalidated()) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.EXPIRED_OTP),
                    HttpStatus.BAD_REQUEST);
        }
        //check expired token
        if (token.getExpiredTime() < new Date().getTime()) {
            resetService.invalidateEntity(tokenOpt.get());
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.EXPIRED_OTP),
                    HttpStatus.BAD_REQUEST);
        }
        //generate new password and set for that account
        String hashedNewPassword = encoder.encode(request.getNewPassword());
        account.setPassword(hashedNewPassword);
        accountService.save(account);
        //invalidate otp token
        resetService.invalidateEntity(token);

        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.AccessControlMessage.RESET_PASSWORD_SUCCESSFULLY),
                HttpStatus.OK);
    }

    @PostMapping(ApiEndPoint.Authentication.GENERATE_OTP_ENDPOINT)
    public ResponseEntity<?> generateOTP(@RequestBody GenerateOTPRequest request) {
        //check existed account
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(request.getEmail());
        if (!accountOpt.isPresent()) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND),
                    HttpStatus.BAD_REQUEST);
        }
        AccountEntity account = accountOpt.get();
        //check existed and invalidated token
        Optional<PasswordResetTokenEntity> tokenOpt = resetService.findLastValidateTokenByAccountID(account.getId());
        if (!tokenOpt.isPresent()) {
            PasswordResetTokenEntity tokenEntity = this.resetService.createResetToken(account);
            //send mail
            try {

                this.mailService.sendMail(account.getEmail(),
                                ResetPasswordTokenConstants.MAIL_SUBJECT,
                                String.format(ResetPasswordTokenConstants.MAIL_BODY, account.getEmail(), tokenEntity.getOtp()))
                        .exceptionally(throwable -> {
                            log.error(throwable.getMessage());
                            return null;
                        });
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.AccessControlMessage.REQUEST_RESET_PASSWORD_SUCCESSFULLY),
                        HttpStatus.OK);
            } catch (UnsupportedEncodingException | MessagingException ex2) {
                return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Mail.SEND_FAILED), HttpStatus.BAD_REQUEST);
            }


        }
        PasswordResetTokenEntity token = tokenOpt.get();
        //check expired token
        if (token.getExpiredTime() > new Date().getTime()) {
            return GenericResponse.build(String.format(
                            MessageUtil.getMessage(MessageConstant.AccessControlMessage.INTERVAL_OTP_REQUEST),
                            RESET_PASSWORD_TOKEN_EXPIRED_TIME),
                    HttpStatus.BAD_REQUEST);
        } else {
            resetService.invalidateEntity(token);
            PasswordResetTokenEntity newToken = this.resetService.createResetToken(account);
            //send mail
            try {
                this.mailService.sendMail(account.getEmail(),
                                ResetPasswordTokenConstants.MAIL_SUBJECT,
                                String.format(ResetPasswordTokenConstants.MAIL_BODY, account.getEmail(), newToken.getOtp()))
                        .exceptionally(throwable -> {
                            log.error(throwable.getMessage());
                            return null;
                        });
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.AccessControlMessage.REQUEST_RESET_PASSWORD_SUCCESSFULLY),
                        HttpStatus.OK);
            } catch (UnsupportedEncodingException | MessagingException ex2) {
                return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Mail.SEND_FAILED), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
