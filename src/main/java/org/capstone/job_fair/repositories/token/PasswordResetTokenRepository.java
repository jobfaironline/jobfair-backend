package org.capstone.job_fair.repositories.token;

import org.capstone.job_fair.models.entities.token.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, String> {

    Optional<PasswordResetTokenEntity> findFirstByAccount_IdOrderByExpiredTimeDesc(String accountID);

    Optional<PasswordResetTokenEntity> findByOtpAndAccount_Id(String otp, String accountID);
}
