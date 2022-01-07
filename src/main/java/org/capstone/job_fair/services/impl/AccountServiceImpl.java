package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.Account;
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
    public List<Account> getAllAccounts() {
        return accountRepository.findAll(); //can be sorted before return...
    }

    public Optional<Account> getActiveAccountByEmail(String email){
        return accountRepository.findAccountByEmailAndStatusNot(email, AccountConstant.ACTIVE);
    }
}
