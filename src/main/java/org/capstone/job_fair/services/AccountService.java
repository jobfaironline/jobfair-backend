package org.capstone.job_fair.services;

import org.capstone.job_fair.models.dtos.AccountEntityDto;
import org.capstone.job_fair.models.entities.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountEntity> getAllAccounts();

    Optional<AccountEntity> getActiveAccountByEmail(String email);

    AccountEntity save(AccountEntity account);

    void createNewAccount(AccountEntityDto dto);
}
