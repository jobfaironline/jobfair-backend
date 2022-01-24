package org.capstone.job_fair.repositories.token;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.token.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.RESET_PASSWORD)
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, String> {

    @Query("select p from PasswordResetTokenEntity p where p.account.id = ?1 order by p.expiredTime DESC")
    Optional<PasswordResetTokenEntity> findTopByAccount_IdOrderByExpiredTimeDesc(String accountID);
    Optional<PasswordResetTokenEntity> findByOtpAndAccount_Id(String otp, String accountID);
}
