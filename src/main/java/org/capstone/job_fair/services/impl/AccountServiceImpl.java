package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.RoleName;
import org.capstone.job_fair.models.dtos.account.CreateAccountDTO;
import org.capstone.job_fair.models.dtos.account.UpdateAccountDTO;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.GenderEntity;
import org.capstone.job_fair.models.entities.attendant.RoleEntity;
import org.capstone.job_fair.repositories.AccountRepository;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.GenderService;
import org.capstone.job_fair.services.RoleService;
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
    private GenderService genderService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder encoder;

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

    @Override
    public AccountEntity convertCreateAccountDTOToEntity(CreateAccountDTO dto) {
        AccountEntity account = new AccountEntity();
        GenderEntity gender = genderService.findById(dto.getGenderID());
        RoleEntity role = roleService.findById(new Integer(3));
        if (gender == null) {
            return null;
        }
        if (dto.getProfileImageURL().isEmpty()) {
            dto.setProfileImageURL(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        }
        UUID id = UUID.randomUUID();
        account.setId(id.toString());
        account.setEmail(dto.getEmail().trim());
        String hashedPassword = encoder.encode(dto.getPassword().trim());
        account.setPassword(hashedPassword);
        account.setStatus(AccountConstant.ACTIVE);
        account.setPhone(dto.getPhone().trim());
        account.setProfileImageUrl(dto.getProfileImageURL());
        account.setFirstname(dto.getFirstName());
        account.setLastname(dto.getLastName());
        account.setMiddlename(dto.getMiddleName());
        account.setGender(gender);
        account.setRole(role);
        return account;
    }
}
