package org.capstone.job_fair.repositories;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.ACCOUNT)
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    @ApiIgnore
    @RestResource(exported = false)
    Optional<AccountEntity> findAccountByEmailAndStatusNot(String email, int status);
}