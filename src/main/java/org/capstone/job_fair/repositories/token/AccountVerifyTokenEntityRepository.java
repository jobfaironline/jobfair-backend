package org.capstone.job_fair.repositories.token;

import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountVerifyTokenEntityRepository extends JpaRepository<AccountVerifyTokenEntity, String> {
    Optional<AccountVerifyTokenEntity> getFirstByAccountIdOrderByExpiredTimeDesc(String id);

    List<AccountVerifyTokenEntity> findByAccountId(String accountId);

}