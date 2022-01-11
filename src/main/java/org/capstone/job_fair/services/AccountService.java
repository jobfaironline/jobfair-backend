package org.capstone.job_fair.services;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.dtos.account.CreateAccountDTO;
import org.capstone.job_fair.models.dtos.account.UpdateAccountDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountEntity> getAllAccounts();

    Optional<AccountEntity> getActiveAccountByEmail(String email);

    AccountEntity save(AccountEntity account);

    AccountEntity convertCreateAccountDTOToEntity(CreateAccountDTO dto);


    void createNewAccount(AccountDTO dto);
}
