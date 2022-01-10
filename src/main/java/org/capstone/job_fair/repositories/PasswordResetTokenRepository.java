package org.capstone.job_fair.repositories;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.RESET_PASSWORD)
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, String> {

    PasswordResetTokenEntity findByAccount_Id(String accountID);
}
