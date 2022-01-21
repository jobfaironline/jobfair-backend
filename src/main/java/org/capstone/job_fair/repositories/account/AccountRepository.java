package org.capstone.job_fair.repositories.account;

import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@Transactional
@ApiIgnore
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.ACCOUNT, exported = false)
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByEmailAndStatus(String email, AccountStatus status);
    Optional<AccountEntity> findByIdAndStatus(String id, AccountStatus status);
    Integer countByEmailAndStatus(String email, AccountStatus status);
    Integer countByIdAndStatus(String id, AccountStatus status);
    Integer countByIdAndEmailAndStatus(String id, String email, AccountStatus status);
}
