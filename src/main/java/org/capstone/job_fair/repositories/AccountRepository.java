package org.capstone.job_fair.repositories;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.ACCOUNT)
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByEmailAndStatusNot(String email, Integer status);
}
