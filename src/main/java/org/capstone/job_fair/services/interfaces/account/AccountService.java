package org.capstone.job_fair.services.interfaces.account;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountDTO> getAllAccounts();

    Optional<AccountEntity> getActiveAccountByEmail(String email);

    AccountEntity save(AccountEntity account);

    Optional<AccountEntity> getActiveAccountById(String id);

    Optional<AccountDTO> getAccountById(String id);


    Integer getCountActiveAccountById(String id);


    Integer getCountAccountByEmail(String email);

    void activateAccount(String id);

    String getIdByEmail(String email);

}
