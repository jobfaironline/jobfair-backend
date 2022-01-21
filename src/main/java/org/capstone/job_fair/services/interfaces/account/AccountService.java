package org.capstone.job_fair.services.interfaces.account;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.enums.Role;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountDTO> getAllAccounts();

    Optional<AccountEntity> getActiveAccountByEmail(String email);

    AccountEntity save(AccountEntity account);

    Optional<AccountEntity> getActiveAccountById(String id);

    Optional<AccountDTO> getActiveAccountDTOByEmail(String email);

    Integer getCountByActiveEmail(String email);

    Integer getCountActiveAccountById(String id);

    List<AccountDTO> getActiveAccountDTOByRole(Role role);

}
