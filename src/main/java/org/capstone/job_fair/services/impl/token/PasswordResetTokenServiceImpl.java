package org.capstone.job_fair.services.impl.token;

import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.token.PasswordResetTokenEntity;
import org.capstone.job_fair.repositories.token.PasswordResetTokenRepository;
import org.capstone.job_fair.services.interfaces.token.PasswordResetTokenService;
import org.capstone.job_fair.utils.OTPGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository resetRepository;

    @Value("${reset-password-expiration}")
    private String RESET_PASSWORD_TOKEN_EXPIRED_TIME;


    @Override
    public Optional<PasswordResetTokenEntity> findLastValidateTokenByAccountID(String accountID) {
        return resetRepository.findFirstByAccount_IdOrderByExpiredTimeDesc(accountID);
    }

    @Override
    @Transactional
    public PasswordResetTokenEntity createResetToken(AccountEntity account) {
        //generate otp
        String otp = OTPGenerator.generateOTP();
        //generate token
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setOtp(otp);
        resetToken.setCreateTime(new Date().getTime());
        resetToken.setExpiredTime(resetToken.getCreateTime() + (long) Integer.parseInt(RESET_PASSWORD_TOKEN_EXPIRED_TIME) * 1000 * 1000);
        resetToken.setAccount(account);
        resetToken.setInvalidated(false);
        resetRepository.save(resetToken);
        return resetToken;
    }

    @Override
    @Transactional
    public void invalidateEntity(PasswordResetTokenEntity entity) {
        entity.setInvalidated(true);
        resetRepository.save(entity);
    }

    @Override
    public Optional<PasswordResetTokenEntity> findTokenByOTPAndAccountID(String otp, String accountID) {
        return resetRepository.findByOtpAndAccount_Id(otp, accountID);
    }

}
