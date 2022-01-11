package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.repositories.AccountRepository;
import org.capstone.job_fair.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<AccountEntity> getActiveAccountByEmail(String email) {
        return accountRepository.findByEmailAndStatusNot(email, AccountConstant.INACTIVE);
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

}
