package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.mappers.AccountEntityMapper;
import org.capstone.job_fair.repositories.AccountRepository;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AttendantRepository attendantRepository;

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
        return accountRepository.findByEmailAndStatus(email, AccountStatus.ACTIVE);
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public void createNewAccount(AccountDTO dto) {
        AccountEntity entity = mapper.toEntity(dto);
        entity.setId(UUID.randomUUID().toString());
        entity.setStatus(AccountStatus.ACTIVE);
        entity.setPassword(encoder.encode(entity.getPassword()));
        entity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);


        RoleEntity role = new RoleEntity();
        role.setId(Role.ATTENDANT.ordinal());
        entity.setRole(role);

        GenderEntity gender = new GenderEntity();
        gender.setId(dto.getGender().ordinal());
        entity.setGender(gender);
        accountRepository.save(entity);

        AttendantEntity attendant = new AttendantEntity();
        attendant.setAccount(entity);
        attendant.setAccountId(entity.getId());
        attendantRepository.save(attendant);
    }

}
