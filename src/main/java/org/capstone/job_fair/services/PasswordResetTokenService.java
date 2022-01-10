package org.capstone.job_fair.services;

import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;

import java.util.UUID;

public interface PasswordResetTokenService {
    PasswordResetTokenEntity findTokenByEmail(String email);
    Boolean saveToken(PasswordResetTokenEntity token);
}
