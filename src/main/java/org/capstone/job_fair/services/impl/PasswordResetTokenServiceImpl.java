package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;
import org.capstone.job_fair.repositories.PasswordResetTokenRepository;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.PasswordResetTokenService;
import org.capstone.job_fair.utils.OTPGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository resetRepository;

    @Autowired
    private AccountService accountService;

    @Value("${reset-password-expiration}")
    private String RESET_PASSWORD_TOKEN_EXPIRED_TIME;


    @Override
    public Optional<PasswordResetTokenEntity> findLastValidateTokenByEmail(String email) {

        Optional<AccountEntity> optAccount = accountService.getActiveAccountByEmail(email);
        if (optAccount.isPresent()) {
            PasswordResetTokenEntity result = resetRepository.findTopByAccount_IdOrderByExpiredTimeDesc(optAccount.get().getId());
            return Optional.ofNullable(result);
        }
        return Optional.empty();
    }

    @Override
    public PasswordResetTokenEntity createResetToken(AccountEntity account) {
        //generate otp
        String otp = OTPGenerator.generateOTP();
        //generate token
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        UUID id = UUID.randomUUID();
        resetToken.setId(id.toString());
        resetToken.setOtp(otp);
        resetToken.setCreateTime(new Date().getTime());
        resetToken.setExpiredTime(resetToken.getExpiredTime() + Integer.parseInt(RESET_PASSWORD_TOKEN_EXPIRED_TIME));
        resetToken.setAccount(account);
        resetToken.setInvalidated(false);
        resetRepository.save(resetToken);
        return resetToken;
    }

    @Override
    public void invalidateEntity(PasswordResetTokenEntity entity) {
        entity.setInvalidated(true);
        resetRepository.save(entity);
    }

    @Override
    public Optional<PasswordResetTokenEntity> findTokenByOTPAndAccountID(String otp, String accountID) {
        return resetRepository.findByOtpAndAccount_Id(otp, accountID);
    }

}
