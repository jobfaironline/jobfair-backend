package org.capstone.job_fair.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;
import org.capstone.job_fair.payload.ResetPasswordTokenRequest;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.MailService;
import org.capstone.job_fair.services.PasswordResetTokenService;
import org.capstone.job_fair.utils.OTPGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class ResetPasswordController {

    @Value("${reset-password-expiration}")
    private String RESET_PASSWORD_TOKEN_EXPIRED_TIME;

    @Autowired
    private  PasswordResetTokenService resetService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MailService mailService;

    @PostMapping(ApiEndPoint.ResetPasswordToken.GENERATE_OTP_ENDPOINT)
    public ResponseEntity<?> generateOTP(@RequestBody ResetPasswordTokenRequest request) {
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(request.getEmail());
        if (!accountOpt.isPresent()) {
            return new ResponseEntity<>("Not found account with email: " + request.getEmail(), HttpStatus.NOT_FOUND);
        }

        try {
            PasswordResetTokenEntity token = resetService.findTokenByEmail(request.getEmail());
            if (token.getExpiredTime() > new Date().getTime()) {
                return new
                        ResponseEntity<>("Cannot request OTP for this email 2 times in " + " minutes", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {

        }
        //generate otp
        char[] otp = OTPGenerator.generateOTP();
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        UUID id=UUID.randomUUID();
        resetToken.setId(id.toString());
        resetToken.setOtp(otp);
        resetToken.setCreateTime(new Date().getTime());
        resetToken.setExpiredTime(resetToken.getExpiredTime() + Integer.parseInt(RESET_PASSWORD_TOKEN_EXPIRED_TIME));
        resetToken.setAccount(accountOpt.get());
        resetToken.setInvalidated(false);
        //save token
        this.resetService.saveToken(resetToken);
        //send mail
        this.mailService.sendMail("phamcaoson1999@gmail.com", otp.toString(), "Test");
        return new ResponseEntity<>("An email has been send to your email", HttpStatus.OK);
    }
}
