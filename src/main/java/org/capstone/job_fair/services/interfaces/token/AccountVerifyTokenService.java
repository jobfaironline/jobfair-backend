package org.capstone.job_fair.services.interfaces.token;

import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.capstone.job_fair.models.entities.token.PasswordResetTokenEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface AccountVerifyTokenService {
    public AccountVerifyTokenEntity createToken (String id);

    public Optional<AccountVerifyTokenEntity> getLastedToken(String id);

    void invalidateEntity(AccountVerifyTokenEntity entity);


}
