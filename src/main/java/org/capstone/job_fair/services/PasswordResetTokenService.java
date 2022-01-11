package org.capstone.job_fair.services;

import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.token.PasswordResetTokenEntity;

import java.util.Optional;

public interface PasswordResetTokenService {
    Optional<PasswordResetTokenEntity> findLastValidateTokenByEmail(String email);

    PasswordResetTokenEntity createResetToken(AccountEntity account);

    void invalidateEntity(PasswordResetTokenEntity entity);


    Optional<PasswordResetTokenEntity> findTokenByOTPAndAccountID(String otp, String accountID);
}
