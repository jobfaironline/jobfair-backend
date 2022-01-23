package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.mappers.CompanyEmployeeEntityMapper;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyEmployeeServiceImpl implements CompanyEmployeeService {
    @Autowired
    private CompanyEmployeeEntityMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private CompanyEmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public void createNewCompanyManagerAccount(CompanyEmployeeDTO dto) {
        dto.getAccount().setRole(Role.COMPANY_MANAGER);
        CompanyEmployeeEntity entity = mapper.toEntity(dto);

        AccountEntity accountEntity = entity.getAccount();
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        accountEntity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        accountEntity.setStatus(AccountStatus.ACTIVE);

        employeeRepository.save(entity);

    }

    @Override
    public void createNewCompanyEmployeeAccount(CompanyEmployeeDTO dto) {
        dto.getAccount().setRole(Role.COMPANY_EMPLOYEE);
        CompanyEmployeeEntity entity = mapper.toEntity(dto);

        AccountEntity accountEntity = entity.getAccount();
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        accountEntity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        accountEntity.setStatus(AccountStatus.REGISTERED);
        employeeRepository.save(entity);
    }

    @Override
    public void updateProfile(CompanyEmployeeDTO dto) {
        try {
            employeeRepository.findById(dto.getAccountId()).map((atd) -> {
                mapper.updateCompanyEmployeeMapperFromDto(dto, atd);
                if (dto.getCompanyDTO() != null){
                    CompanyEntity companyEntity = companyRepository.getById(dto.getCompanyDTO().getId());
                    atd.setCompany(companyEntity);
                }
                return employeeRepository.save(atd);
            }).orElseThrow(NoSuchElementException::new);
        } catch (EntityNotFoundException e){
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<CompanyEmployeeDTO> getAllCompanyEmployees(String id) {
        return employeeRepository.findAllByCompanyId(id).stream().map(companyEmployeeEntity -> {
            return mapper.toDTO(companyEmployeeEntity);
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteEmployee(String id) {
       Optional<CompanyEmployeeEntity> companyEmployeeEntity = employeeRepository.findById(id);
       if(!companyEmployeeEntity.isPresent()) return false;
        companyEmployeeEntity.get().getAccount().setStatus(AccountStatus.INACTIVE);
        employeeRepository.save(companyEmployeeEntity.get());
       return true;
    }

    @Override
    public void updateEmployeeStatus(String email){
        AccountEntity accountEntity = accountRepository.findByEmail(email).get();
        accountEntity.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(accountEntity);
    }

}
