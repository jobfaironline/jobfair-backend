package org.capstone.job_fair.services.interfaces.token;

import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.token.PasswordResetTokenEntity;

import java.util.Optional;

public interface PasswordResetTokenService {
    Optional<PasswordResetTokenEntity> findLastValidateTokenByAccountID(String accountID);

    PasswordResetTokenEntity createResetToken(AccountEntity account);

    void invalidateEntity(PasswordResetTokenEntity entity);


    Optional<PasswordResetTokenEntity> findTokenByOTPAndAccountID(String otp, String accountID);
}
