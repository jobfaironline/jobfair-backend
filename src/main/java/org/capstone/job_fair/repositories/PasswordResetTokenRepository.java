package org.capstone.job_fair.repositories;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.RESET_PASSWORD)
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, String> {

    PasswordResetTokenEntity findByAccount_Id(String accountID);
    Optional<PasswordResetTokenEntity> findByOtpAndAccount_Id(String otp, String accountID);
}
