package org.capstone.job_fair.repositories.account;

import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    @Query("select t.id from AccountEntity t where t.role.id = :role")
    List<String> findAccountByRole(@Param("role") int id);

    Optional<AccountEntity> findByEmailAndStatus(String email, AccountStatus status);

    Optional<AccountEntity> findByIdAndStatus(String id, AccountStatus status);

    Integer countByIdAndStatus(String id, AccountStatus status);

    Integer countByEmail(String email);

    Optional<AccountEntity> findByEmail(String email);

    Optional<AccountEntity> findByEmailAndStatusIn(String email, List<AccountStatus> statuses);
}
