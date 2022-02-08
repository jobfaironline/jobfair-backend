package org.capstone.job_fair.repositories.token;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

@Transactional
@ApiIgnore
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.VERIFY, exported = false)
public interface AccountVerifyTokenEntityRepository extends JpaRepository<AccountVerifyTokenEntity, String> {
    Optional<AccountVerifyTokenEntity> getFirstByAccountIdOrderByExpiredTimeDesc(String id);

    List<AccountVerifyTokenEntity> findByAccountId(String accountId);

}