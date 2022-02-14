package org.capstone.job_fair.services.impl.token;

import org.capstone.job_fair.models.dtos.token.AccountVerifyTokenDTO;
import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.capstone.job_fair.repositories.token.AccountVerifyTokenEntityRepository;
import org.capstone.job_fair.services.interfaces.token.AccountVerifyTokenService;
import org.capstone.job_fair.services.mappers.AccountVerifyTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountVerifyTokenServiceImpl implements AccountVerifyTokenService {

    @Value("${verify-account-expiration}")
    private String TOKEN_EXPIRED_TIME;

    @Autowired
    private AccountVerifyTokenEntityRepository accountVerifyTokenEntityRepository;

    @Autowired
    private AccountVerifyTokenMapper mapper;

    @Override
    public AccountVerifyTokenDTO createToken(String userId) {
        //generate token
        AccountVerifyTokenEntity accountVerifyToken = new AccountVerifyTokenEntity();
        accountVerifyToken.setCreatedTime(new Date().getTime());
        accountVerifyToken.setExpiredTime(accountVerifyToken.getCreatedTime() + (long) Integer.parseInt(TOKEN_EXPIRED_TIME) * 1000 * 1000);
        accountVerifyToken.setAccountId(userId);
        accountVerifyToken.setIsInvalidated(false);
        accountVerifyTokenEntityRepository.save(accountVerifyToken);

        return mapper.toAccountVerifyTokenDto(accountVerifyToken);
    }

    @Override
    public AccountVerifyTokenDTO getLastedToken(String id) {
        Optional<AccountVerifyTokenEntity> entity = accountVerifyTokenEntityRepository.getFirstByAccountIdOrderByExpiredTimeDesc(id);
        if (!entity.isPresent()) return null;
        return mapper.toAccountVerifyTokenDto(entity.get());
    }

    @Override
    public void invalidateEntity(AccountVerifyTokenEntity entity) {
        entity.setIsInvalidated(true);
        accountVerifyTokenEntityRepository.save(entity);
    }

    @Override
    public void invalidateTokenById(String tokenId) {
        accountVerifyTokenEntityRepository.findById(tokenId).ifPresent(entity -> {
            entity.setIsInvalidated(true);
            accountVerifyTokenEntityRepository.save(entity);
        });
    }
}
