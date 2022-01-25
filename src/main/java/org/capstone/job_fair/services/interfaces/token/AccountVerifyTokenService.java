package org.capstone.job_fair.services.interfaces.token;

import org.capstone.job_fair.models.dtos.token.AccountVerifyTokenDTO;
import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;

import java.util.Optional;

public interface AccountVerifyTokenService {
    public AccountVerifyTokenDTO createToken (String id);

    public AccountVerifyTokenDTO getLastedToken(String id);

    void invalidateEntity(AccountVerifyTokenEntity entity);


}
