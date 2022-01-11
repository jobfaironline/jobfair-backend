package org.capstone.job_fair.repositories;

import org.capstone.job_fair.constants.AccountStatus;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@Transactional
@ApiIgnore
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.ACCOUNT, exported = false)
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByEmailAndStatusNot(String email, AccountStatus status);
}
