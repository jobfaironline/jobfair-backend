package org.capstone.job_fair.services;

import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenService {
    Optional<PasswordResetTokenEntity> findTokenByEmail(String email);

    PasswordResetTokenEntity createResetToken(AccountEntity account);

    void invalidateEntity(PasswordResetTokenEntity entity);


    Optional<PasswordResetTokenEntity> findTokenByOTPAndAccountID(String otp, String accountID);
}
