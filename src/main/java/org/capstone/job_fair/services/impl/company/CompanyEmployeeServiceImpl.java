package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.services.mappers.CompanyEmployeeEntityMapper;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CompanyEmployeeServiceImpl implements CompanyEmployeeService {
    @Autowired
    private CompanyEmployeeEntityMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private CompanyEmployeeRepository employeeRepository;

    @Override
    public void createNewCompanyManagerAccount(CompanyEmployeeDTO dto) {
        String id = UUID.randomUUID().toString();
        CompanyEmployeeEntity entity = mapper.toEntity(dto);
        entity.setAccountId(id);

        AccountEntity accountEntity = entity.getAccount();
        accountEntity.setId(id);
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        accountEntity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        accountEntity.setStatus(AccountStatus.ACTIVE);

        RoleEntity role = new RoleEntity();
        role.setId(Role.COMPANY_MANAGER.ordinal());
        accountEntity.setRole(role);

        GenderEntity gender = new GenderEntity();
        gender.setId(dto.getAccount().getGender().ordinal());
        accountEntity.setGender(gender);

        employeeRepository.save(entity);

    }
}
