package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.mappers.CompanyEmployeeMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyEmployeeServiceImpl implements CompanyEmployeeService {
    @Autowired
    private CompanyEmployeeMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private CompanyEmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;


    private boolean isEmailExist(String email) {
        return accountRepository.countByEmail(email) != 0;
    }

    @Override
    public CompanyEmployeeDTO createNewCompanyManagerAccount(CompanyEmployeeDTO dto) {
        dto.getAccount().setRole(Role.COMPANY_MANAGER);
        CompanyEmployeeEntity entity = mapper.toEntity(dto);

        AccountEntity accountEntity = entity.getAccount();
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        accountEntity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        accountEntity.setStatus(AccountStatus.REGISTERED);

        employeeRepository.save(entity);
        return mapper.toDTO(entity);

    }


    @Override
    public void createNewCompanyEmployeeAccount(CompanyEmployeeDTO dto) {
        if (isEmailExist(dto.getAccount().getEmail())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_EXISTED));
        }
        CompanyEntity companyEntity = null;
        try {
            companyEntity = companyRepository.getById(dto.getCompanyDTO().getId());
        } catch (EntityNotFoundException ex) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND));
        }
        int currentCompanyEmployeeNum = employeeRepository.countByCompanyIdAndAccountStatusIn(companyEntity.getId(),
                Arrays.asList(AccountStatus.VERIFIED, AccountStatus.REGISTERED));
        if (currentCompanyEmployeeNum >= companyEntity.getEmployeeMaxNum()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.MAX_QUOTA_FOR_COMPANY_EMPLOYEE));
        }

        String hashPassword = encoder.encode(PasswordGenerator.generatePassword());
        dto.getAccount().setRole(Role.COMPANY_EMPLOYEE);
        dto.getAccount().setPassword(hashPassword);
        dto.getAccount().setStatus(AccountStatus.REGISTERED);
        dto.getAccount().setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);

        CompanyEmployeeEntity entity = mapper.toEntity(dto);
        employeeRepository.save(entity);
    }

    @Override
    public void updateProfile(CompanyEmployeeDTO dto) {
        try {
            employeeRepository.findById(dto.getAccountId()).map((atd) -> {
                mapper.updateCompanyEmployeeMapperFromDto(dto, atd);
                if (dto.getCompanyDTO() != null) {
                    CompanyEntity companyEntity = companyRepository.getById(dto.getCompanyDTO().getId());
                    atd.setCompany(companyEntity);
                }
                return employeeRepository.save(atd);
            }).orElseThrow(NoSuchElementException::new);
        } catch (EntityNotFoundException e) {
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
        if (!companyEmployeeEntity.isPresent()) return false;
        companyEmployeeEntity.get().getAccount().setStatus(AccountStatus.INACTIVE);
        employeeRepository.save(companyEmployeeEntity.get());
        return true;
    }

    @Override
    public void updateEmployeeStatus(String email) {
        AccountEntity accountEntity = accountRepository.findByEmail(email).get();
        accountEntity.setStatus(AccountStatus.VERIFIED);
        accountRepository.save(accountEntity);
    }

    @Override
    @Transactional
    public void promoteEmployee(String employeeId, String managerId) {

        CompanyEmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND)));
        CompanyEmployeeEntity manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.MANAGER_NOT_FOUND)));


        if (employee.getAccount().getStatus() == AccountStatus.REGISTERED ||
                employee.getAccount().getStatus() == AccountStatus.INACTIVE) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_ACTIVE));
        }
        if (employee.getAccount().getRole().getId() != Role.COMPANY_EMPLOYEE.ordinal()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.INVALID_ROLE));
        }
        if (manager.getAccount().getRole().getId() != Role.COMPANY_MANAGER.ordinal()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.INVALID_ROLE));
        }
        System.out.println(employee.getCompany().equals(manager.getCompany()));
        if (!manager.getCompany().equals(employee.getCompany())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.DIFFERENT_COMPANY_ERROR));
        }

        employee.getAccount().setRole(new RoleEntity(Role.COMPANY_MANAGER.ordinal()));
        manager.getAccount().setRole(new RoleEntity(Role.COMPANY_EMPLOYEE.ordinal()));
        employeeRepository.save(employee);
        employeeRepository.save(manager);
    }


}
