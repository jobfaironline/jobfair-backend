package org.capstone.job_fair.services;

import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenService {
    PasswordResetTokenEntity findTokenByEmail(String email);
    Boolean saveToken(PasswordResetTokenEntity token);
    Optional<PasswordResetTokenEntity> findTokenByOTPAndAccountID(String otp, String accountID);
}
