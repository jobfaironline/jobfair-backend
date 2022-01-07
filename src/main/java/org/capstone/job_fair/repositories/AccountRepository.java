package org.capstone.job_fair.repositories;

import org.capstone.job_fair.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findAccountByEmailAndStatusNot(String email, int status);

}
