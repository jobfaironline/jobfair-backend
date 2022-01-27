package org.capstone.job_fair.services.impl.account;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.mappers.AccountMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream().map(AccountEntity -> accountMapper.toDTO(AccountEntity))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountEntity> getActiveAccountByEmail(String email) {
        return accountRepository.findByEmailAndStatus(email, AccountStatus.VERIFIED);
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<AccountEntity> getActiveAccountById(String id) {
        return accountRepository.findByIdAndStatus(id, AccountStatus.VERIFIED);
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
        return accountRepository.countByIdAndStatus(id, AccountStatus.VERIFIED);
    }


    @Override
    public Integer getCountAccountByEmail(String email) {
        return accountRepository.countByEmail(email);
    }

    @Override
    public void activateAccount(String id) {
        AccountEntity accountEntity = accountRepository.findById(id).get();
        accountEntity.setStatus(AccountStatus.VERIFIED);
        accountRepository.save(accountEntity);
    }

    @Override
    public String getIdByEmail(String email) {
        String id = accountRepository.findByEmail(email).get().getId().toString();
        return id;
    }

    @Override
    @Transactional
    public void changePassword(String newPassword, String oldPassword){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!encoder.matches(oldPassword, userDetails.getPassword())){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.OLD_PASSWORD_MISMATCH));
        }
        AccountEntity entity = accountRepository.findById(userDetails.getId()).get();
        String hashedPassword = encoder.encode(newPassword);
        entity.setPassword(hashedPassword);
        accountRepository.save(entity);
    }

}
