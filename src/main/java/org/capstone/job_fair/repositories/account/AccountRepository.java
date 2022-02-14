package org.capstone.job_fair.repositories.account;

import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByEmailAndStatus(String email, AccountStatus status);

    Optional<AccountEntity> findByIdAndStatus(String id, AccountStatus status);

    Integer countByIdAndStatus(String id, AccountStatus status);

    Integer countByEmail(String email);

    Optional<AccountEntity> findByEmail(String email);

    Optional<AccountEntity> findByEmailAndStatusIn(String email, List<AccountStatus> statuses);
}
