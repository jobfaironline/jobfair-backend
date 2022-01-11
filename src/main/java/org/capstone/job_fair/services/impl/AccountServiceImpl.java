package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.AccountStatus;
import org.capstone.job_fair.constants.Role;
import org.capstone.job_fair.models.dtos.AccountEntityDto;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.GenderEntity;
import org.capstone.job_fair.models.entities.attendant.RoleEntity;
import org.capstone.job_fair.models.mappers.AccountEntityMapper;
import org.capstone.job_fair.repositories.AccountRepository;
import org.capstone.job_fair.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountEntityMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<AccountEntity> getActiveAccountByEmail(String email) {
        return accountRepository.findByEmailAndStatusNot(email, AccountStatus.ACTIVE);
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public void createNewAccount(AccountEntityDto dto) {
        AccountEntity entity = mapper.toEntity(dto);
        entity.setId(UUID.randomUUID().toString());
        entity.setStatus(AccountStatus.ACTIVE);

        RoleEntity role = new RoleEntity();
        role.setId(Role.ATTENDANT.ordinal());
        entity.setRole(role);
        GenderEntity gender = new GenderEntity();
        gender.setId(1);
        entity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);

        entity.setGender(gender);
        accountRepository.save(entity);
    }

}
