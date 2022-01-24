package org.capstone.job_fair.services.impl.account;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.mappers.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream().map(AccountEntity -> accountMapper.toDTO(AccountEntity))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountEntity> getActiveAccountByEmail(String email) {
        return accountRepository.findByEmailAndStatus(email, AccountStatus.ACTIVE);
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<AccountEntity> getActiveAccountById(String id) {
        return accountRepository.findByIdAndStatus(id, AccountStatus.ACTIVE);
    }

    @Override
    public Optional<AccountDTO> getAccountById(String id) {
        Optional<AccountEntity> opt = accountRepository.findById(id);
        if (!opt.isPresent()){
            return Optional.empty();
        }
        AccountDTO accountDTO = accountMapper.toDTO(opt.get());
        return Optional.of(accountDTO);
    }

    @Override
    public Integer getCountActiveAccountById(String id) {
        return accountRepository.countByIdAndStatus(id, AccountStatus.ACTIVE);
    }


    @Override
    public Integer getCountAccountByEmail(String email) {
        return accountRepository.countByEmail(email);
    }

    @Override
    public void changeAccountStatus(String id) {
        AccountEntity accountEntity = accountRepository.findById(id).get();
        accountEntity.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(accountEntity);
    }


}
