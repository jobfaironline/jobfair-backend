package org.capstone.job_fair.services.impl.company;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.CSVConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyEmployeeCSVRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CreateJobPositionRequest;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Service
@Transactional(readOnly = true)
@Slf4j
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

    @Autowired
    private Validator validator;

    @Autowired
    private MailService mailService;


    private boolean isEmailExist(String email) {
        return accountRepository.countByEmail(email) != 0;
    }

    @Override
    @Transactional
    public CompanyEmployeeDTO createNewCompanyManagerAccount(CompanyEmployeeDTO dto) {
        dto.getAccount().setRole(Role.COMPANY_MANAGER);
        dto.getAccount().setCreateTime(new Date().getTime());
        CompanyEmployeeEntity entity = mapper.toEntity(dto);

        AccountEntity accountEntity = entity.getAccount();
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        accountEntity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        accountEntity.setStatus(AccountStatus.REGISTERED);

        employeeRepository.save(entity);
        return mapper.toDTO(entity);
    }


    @Override
    @Transactional
    public void createNewCompanyEmployeeAccount(CompanyEmployeeDTO dto) {
        if (isEmailExist(dto.getAccount().getEmail())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_EXISTED));
        }
        CompanyEntity companyEntity;
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

        String password = PasswordGenerator.generatePassword();
        dto.getAccount().setPassword(password);
        dto.getAccount().setRole(Role.COMPANY_EMPLOYEE);
        dto.getAccount().setStatus(AccountStatus.REGISTERED);
        dto.getAccount().setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        dto.getAccount().setCreateTime(new Date().getTime());
        dto.getAccount().setGender(Gender.MALE);

        CompanyEmployeeEntity entity = mapper.toEntity(dto);
        String hashPassword = encoder.encode(password);
        entity.getAccount().setPassword(hashPassword);
        employeeRepository.save(entity);
    }

    @Override
    @Transactional
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
    public Page<CompanyEmployeeDTO> getAllCompanyEmployees(String companyId, String searchContent, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        return employeeRepository.findAllByAccountFirstnameContainsOrAccountMiddlenameContainsOrAccountLastnameContainsOrEmployeeIdContainsAndCompanyId
                        (searchContent, searchContent, searchContent, searchContent, companyId, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)))
                .map(mapper::toDTO);
    }

    @Override
    @Transactional
    public Boolean deleteEmployee(String id) {
        Optional<CompanyEmployeeEntity> companyEmployeeEntity = employeeRepository.findById(id);
        if (!companyEmployeeEntity.isPresent()) return false;
        companyEmployeeEntity.get().getAccount().setStatus(AccountStatus.INACTIVE);
        employeeRepository.save(companyEmployeeEntity.get());
        return true;
    }

    @Override
    @Transactional
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
        if (!manager.getCompany().equals(employee.getCompany())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyEmployee.DIFFERENT_COMPANY_ERROR));
        }

        employee.getAccount().setRole(new RoleEntity(Role.COMPANY_MANAGER.ordinal()));
        manager.getAccount().setRole(new RoleEntity(Role.COMPANY_EMPLOYEE.ordinal()));
        employeeRepository.save(employee);
        employeeRepository.save(manager);
    }

    @Override
    public Optional<CompanyEmployeeDTO> getCompanyEmployeeByAccountId(String accountID) {
        return employeeRepository.findByAccountId(accountID).map(mapper::toDTO);
    }

    @Override
    public CompanyEmployeeDTO getCompanyEmployeeByAccountId(String employeeID, String companyID) {
        Optional<CompanyEmployeeEntity> companyEmployeeEntityOpt = null;
        //if company id not null => the manager is performing the search, then find employee base on id and comany id to make sure that employee is belonged to their company, not the others
        //if company id is null => admin is performing the search, no need to check company id.
        if (companyID != null)
            companyEmployeeEntityOpt = employeeRepository.findByAccountIdAndCompanyId(employeeID, companyID);
        else companyEmployeeEntityOpt = employeeRepository.findById(employeeID);
        if (!companyEmployeeEntityOpt.isPresent()) return null;
        return mapper.toDTO(companyEmployeeEntityOpt.get());
    }

    @Override
    public Integer getCompanyEmployeeCount(String companyId) {
        return employeeRepository.countByCompanyId(companyId);
    }

    @Override
    @Transactional
    @SneakyThrows
    public List<CompanyEmployeeDTO> createNewCompanyEmployeesFromCSVFile(MultipartFile file, String companyId) {
        List<CompanyEmployeeDTO> result = new ArrayList<>();
        if (!CSVConstant.TYPE.equals(file.getContentType())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.CSV_FILE_ERROR));
        }
        Reader reader = new InputStreamReader(file.getInputStream());
        CsvToBean<CreateCompanyEmployeeCSVRequest> csvToBean = new CsvToBeanBuilder(reader)
                .withType(CreateCompanyEmployeeCSVRequest.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<CreateCompanyEmployeeCSVRequest> companyEmployeeCSVs = csvToBean.iterator();
        int numberOfCreatedJob = 0;
        while (companyEmployeeCSVs.hasNext()) {
            numberOfCreatedJob++;
            CreateCompanyEmployeeCSVRequest companyEmployeeCSVRequest = companyEmployeeCSVs.next();
            Errors errors = new BindException(companyEmployeeCSVRequest, CreateJobPositionRequest.class.getSimpleName());
            validator.validate(companyEmployeeCSVRequest, errors);
            if (errors.hasErrors()) {
                String errorMessage = String.format(MessageUtil.getMessage(MessageConstant.Job.CSV_LINE_ERROR), numberOfCreatedJob);
                throw new IllegalArgumentException(errorMessage);
            }
            CompanyDTO companyDTO = CompanyDTO.builder().id(companyId).build();
            CompanyEmployeeDTO companyEmployeeDTO = mapper.toDTO(companyEmployeeCSVRequest);
            companyEmployeeDTO.setCompanyDTO(companyDTO);
            createNewCompanyEmployeeAccount(companyEmployeeDTO);
            result.add(companyEmployeeDTO);
            //send email
            this.mailService.sendMail(companyEmployeeDTO.getAccount().getEmail(),
                            MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_SUBJECT),
                            MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_CONTENT) + companyEmployeeDTO.getAccount().getPassword())
                    .exceptionally(throwable -> {
                        log.error(throwable.getMessage());
                        return null;
                    });
        }
        return result;
    }


}
