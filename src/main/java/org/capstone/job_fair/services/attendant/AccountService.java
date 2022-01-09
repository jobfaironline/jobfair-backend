package org.capstone.job_fair.services.attendant;

import org.capstone.job_fair.models.entities.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountEntity> getAllAccounts();

    Optional<AccountEntity> getActiveAccountByEmail(String email);
}
