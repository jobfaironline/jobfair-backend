package org.capstone.job_fair.services;

import org.capstone.job_fair.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();

    Optional<Account> getActiveAccountByEmail(String email);
}
