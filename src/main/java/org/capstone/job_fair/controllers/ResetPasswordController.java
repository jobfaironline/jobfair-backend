package org.capstone.job_fair.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ResetPasswordTokenConstants;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;
import org.capstone.job_fair.payload.GenerateOTPRequest;
import org.capstone.job_fair.payload.GenerateOTPResponse;
import org.capstone.job_fair.payload.ResetPasswordRequest;
import org.capstone.job_fair.payload.ResetPasswordResponse;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.MailService;
import org.capstone.job_fair.services.PasswordResetTokenService;
import org.capstone.job_fair.utils.OTPGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ResetPasswordController {

    @Value("${reset-password-expiration}")
    private String RESET_PASSWORD_TOKEN_EXPIRED_TIME;

    @Autowired
    private  PasswordResetTokenService resetService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

//    public ResetPasswordController(PasswordResetTokenService resetService, AccountService accountService, MailService mailService, BCryptPasswordEncoder encoder) {
//        this.resetService = resetService;
//        this.accountService = accountService;
//        this.mailService = mailService;
//        this.encoder = encoder;
//    }

    @PostMapping(ApiEndPoint.ResetPasswordToken.RESET_PASSWORD_ENDPOINT)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        //check if token is expired or not ?
        if (!request.getNewPassword().equals(request.getConfirmPassword())){
            return new ResponseEntity<>("Confirm password must match with new password", HttpStatus.BAD_REQUEST);
        }
        AccountEntity account = getAccountByEmail(request.getEmail());
        if (account == null) {
            return new ResponseEntity<>(
                    new GenerateOTPResponse("Not found account with email: " + request.getEmail()),
                    HttpStatus.NOT_FOUND);
        }
        //check if token and account_id  matched?
        Optional<PasswordResetTokenEntity> opt = resetService.findTokenByOTPAndAccountID(request.getOtp(), account.getId());
        if (!opt.isPresent()) {
            return new ResponseEntity<>("OTP is invalid", HttpStatus.BAD_REQUEST );
        }
        //check if token is expired
        PasswordResetTokenEntity entity = opt.get();
        if (entity.getExpiredTime() > new Date().getTime()) {
            return new ResponseEntity<>("OTP is expired", HttpStatus.BAD_REQUEST );
        }
        //generate new password and set for that account
        String newPassword = encoder.encode(request.getNewPassword());
        account.setPassword(newPassword);
        accountService.save(account);
        //invalidate otp token
        entity.setInvalidated(true);
        resetService.saveToken(entity);
        //return
        ResetPasswordResponse response = new ResetPasswordResponse();
        response.setEmail(request.getEmail());
        response.setNewPassword(request.getNewPassword());
        response.setMessage("Reset password successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(ApiEndPoint.ResetPasswordToken.GENERATE_OTP_ENDPOINT)
    public ResponseEntity<GenerateOTPResponse> generateOTP(@RequestBody GenerateOTPRequest request) {
        if (getAccountByEmail(request.getEmail()) == null) {
            return new ResponseEntity<>(
                    new GenerateOTPResponse("Not found account with email: " + request.getEmail()),
                    HttpStatus.NOT_FOUND);
        }

        try {
            PasswordResetTokenEntity token = resetService.findTokenByEmail(request.getEmail());
            if (token.getExpiredTime() > new Date().getTime()) {
                return new ResponseEntity<>(
                    new GenerateOTPResponse("Cannot request OTP for this email 2 times in " + RESET_PASSWORD_TOKEN_EXPIRED_TIME +" minutes"),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {

        }
        //generate otp
        String otp = OTPGenerator.generateOTP();
        //generate token
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        UUID id=UUID.randomUUID();
        resetToken.setId(id.toString());
        resetToken.setOtp(otp);
        resetToken.setCreateTime(new Date().getTime());
        resetToken.setExpiredTime(resetToken.getExpiredTime() + Integer.parseInt(RESET_PASSWORD_TOKEN_EXPIRED_TIME));
        resetToken.setAccount(getAccountByEmail(request.getEmail()));
        resetToken.setInvalidated(false);
        //save token
        this.resetService.saveToken(resetToken);
        //send mail
        this.mailService.sendMail(ResetPasswordTokenConstants.MailConstant.TO_EMAIL,
                ResetPasswordTokenConstants.MailConstant.MAIL_SUBJECT,
                ResetPasswordTokenConstants.MailConstant.MAIL_BODY + otp);
        return new ResponseEntity<>(
                new GenerateOTPResponse("An email has been sent to your email"), HttpStatus.OK);
    }

    private AccountEntity getAccountByEmail(String email) {
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(email);
        return accountOpt.isPresent() ? accountOpt.get() : null;
    }
}
