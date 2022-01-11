package org.capstone.job_fair.controllers;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ResetPasswordTokenConstants;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;
import org.capstone.job_fair.payload.*;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.MailService;
import org.capstone.job_fair.services.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

@RestController
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

    private static final String CONFIRM_PASSWORD_MISMATCH_MESSAGE = "Confirm password must match with new password";
    private static final String INVALID_OTP_MESSAGE = "OTP is invalid";
    private static final String EXPIRED_OTP_MESSAGE = "OTP is invalid";
    private static final String RESET_SUCCESSFULLY_MESSAGE = "Reset password successfully";
    private final String INTERVAL_REQUEST_MESSAGE = "Cannot request OTP for this email 2 times in " + RESET_PASSWORD_TOKEN_EXPIRED_TIME + " minutes";
    private static final String REQUEST_RESET_SUCCESSFULLY_MESSAGE = "An email has been sent to your email";
    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";


    @PostMapping(ApiEndPoint.ResetPasswordToken.RESET_PASSWORD_ENDPOINT)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        //check matching password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return new GenericMessageResponseEntity(CONFIRM_PASSWORD_MISMATCH_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        //check existed account
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(request.getEmail());
        if (!accountOpt.isPresent()) {
            return new GenericMessageResponseEntity(ACCOUNT_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        AccountEntity account = accountOpt.get();
        //check existed token
        Optional<PasswordResetTokenEntity> tokenOpt = resetService.findTokenByOTPAndAccountID(request.getOtp(), account.getId());
        if (!tokenOpt.isPresent()) {
            return new GenericMessageResponseEntity(INVALID_OTP_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        PasswordResetTokenEntity token = tokenOpt.get();
        //check if token account is the same as request account
        if (!account.equals(token.getAccount())) {
            return new GenericMessageResponseEntity(INVALID_OTP_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        //check if token is expired
        if (token.getExpiredTime() > new Date().getTime()) {
            return new GenericMessageResponseEntity(EXPIRED_OTP_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        //generate new password and set for that account
        String hashedNewPassword = encoder.encode(request.getNewPassword());
        account.setPassword(hashedNewPassword);
        accountService.save(account);
        //invalidate otp token
        resetService.invalidateEntity(token);

        return new GenericMessageResponseEntity(RESET_SUCCESSFULLY_MESSAGE, HttpStatus.OK);
    }

    @PostMapping(ApiEndPoint.ResetPasswordToken.GENERATE_OTP_ENDPOINT)
    public ResponseEntity<?> generateOTP(@RequestBody GenerateOTPRequest request) {
        //check existed account
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(request.getEmail());
        if (!accountOpt.isPresent()) {
            return new GenericMessageResponseEntity(ACCOUNT_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        AccountEntity account = accountOpt.get();
        //check existed and expired token
        Optional<PasswordResetTokenEntity> tokenOpt = resetService.findTokenByEmail(request.getEmail());
        if (tokenOpt.isPresent() && tokenOpt.get().getExpiredTime() > new Date().getTime()) {
            return new GenericMessageResponseEntity(INTERVAL_REQUEST_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        PasswordResetTokenEntity tokenEntity = this.resetService.createResetToken(account);
        //send mail
        this.mailService.sendMail(account.getEmail(),
                ResetPasswordTokenConstants.MAIL_SUBJECT,
                ResetPasswordTokenConstants.MAIL_BODY + tokenEntity.getOtp());
        return new GenericMessageResponseEntity(REQUEST_RESET_SUCCESSFULLY_MESSAGE, HttpStatus.OK);
    }

}
