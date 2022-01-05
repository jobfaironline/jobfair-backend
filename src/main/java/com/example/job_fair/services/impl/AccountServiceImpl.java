package com.example.job_fair.services.impl;

import com.example.job_fair.models.Account;
import com.example.job_fair.repositories.AccountRepository;
import com.example.job_fair.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll(); //can be sorted before return...
    }
}
