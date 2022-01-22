package org.capstone.job_fair.services.impl.account;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.mappers.AccountEntityMapper;
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
    private AccountEntityMapper mapper;


    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream().map(AccountEntity -> mapper.toDTO(AccountEntity))
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
    public Optional<AccountDTO> getActiveAccountDTOByEmail(String email) {
        return accountRepository.findByEmailAndStatus(email, AccountStatus.ACTIVE)
                .map(entity -> mapper.toDTO(entity));
    }

    @Override
    public Integer getCountByActiveEmail(String email) {
        return accountRepository.countByEmailAndStatus(email, AccountStatus.ACTIVE);
    }

    @Override
    public Integer getCountActiveAccountById(String id) {
        return accountRepository.countByIdAndStatus(id, AccountStatus.ACTIVE);
    }

    @Override
    public List<AccountDTO> getActiveAccountDTOByRole(Role role) {
       return  accountRepository.findAllByRoleAndStatus(role, AccountStatus.ACTIVE)
        .stream()
        .map(entity -> mapper.toDTO(entity)).collect(Collectors.toList());

    }

    @Override
    public Integer getCountAccountByEmail(String email) {
        return accountRepository.countByEmail(email);
    }


}
