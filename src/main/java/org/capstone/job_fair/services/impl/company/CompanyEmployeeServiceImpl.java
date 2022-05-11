package org.capstone.job_fair.services.impl.company;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.CompanyEmployeeConstant;
import org.capstone.job_fair.constants.FileConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyEmployeeCSVRequest;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
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
import org.capstone.job_fair.services.interfaces.util.XSLSFileService;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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
    private XSLSFileService xslsFileService;


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
    public CompanyEmployeeDTO createNewCompanyEmployeeAccount(CompanyEmployeeDTO dto) {
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
        entity = employeeRepository.save(entity);
        return mapper.toDTO(entity);
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


    private ParseFileResult<CompanyEmployeeDTO> createNewEmployeeFromListString(List<List<String>> data, String companyId) {
        ParseFileResult<CompanyEmployeeDTO> parseResult = new ParseFileResult();

        int rowNum = data.size();
        for (int i = 1; i < rowNum; i++) {
            List<String> rowData = data.get(i);
            String firstName = rowData.get(CompanyEmployeeConstant.XLSXFormat.FIRST_NAME_INDEX);
            String middleName = rowData.get(CompanyEmployeeConstant.XLSXFormat.MIDDLE_NAME_INDEX);
            String lastName = rowData.get(CompanyEmployeeConstant.XLSXFormat.LAST_NAME_INDEX);
            String department = rowData.get(CompanyEmployeeConstant.XLSXFormat.DEPARTMENT_INDEX);
            String employeeId = rowData.get(CompanyEmployeeConstant.XLSXFormat.EMPLOYEE_ID_INDEX);
            String email = rowData.get(CompanyEmployeeConstant.XLSXFormat.EMAIL_INDEX);
            CreateCompanyEmployeeCSVRequest companyEmployeeCSVRequest = new CreateCompanyEmployeeCSVRequest(firstName, middleName, lastName, department, employeeId, email);
            Errors errors = new BindException(companyEmployeeCSVRequest, CreateCompanyEmployeeCSVRequest.class.getSimpleName());
            validator.validate(companyEmployeeCSVRequest, errors);
            if (errors.hasErrors()) {
                StringBuilder message = new StringBuilder("");
                for (ObjectError error : errors.getAllErrors()) {
                    message.append(((FieldError) error).getField());
                    message.append(" ");
                    message.append(error.getDefaultMessage());
                    message.append(".");
                }
                parseResult.addErrorMessage(i, message.toString());
                continue;
            }
            CompanyDTO companyDTO = CompanyDTO.builder().id(companyId).build();
            CompanyEmployeeDTO companyEmployeeDTO = mapper.toDTO(companyEmployeeCSVRequest);
            companyEmployeeDTO.setCompanyDTO(companyDTO);
            parseResult.addToResult(companyEmployeeDTO);
        }
        List<CompanyEmployeeDTO> insertResult = new ArrayList<>();
        if (!parseResult.isHasError()) {
            for (int i = 0; i < parseResult.getResult().size(); i++) {
                CompanyEmployeeDTO companyEmployeeDTO = parseResult.getResult().get(i);
                try {
                    companyEmployeeDTO = this.createNewCompanyEmployeeAccount(companyEmployeeDTO);
                    insertResult.add(companyEmployeeDTO);
                } catch (IllegalArgumentException e) {
                    //+1 because row start at 1
                    parseResult.addErrorMessage(i + 1, e.getMessage());
                } catch (DataIntegrityViolationException e){
                    parseResult.addErrorMessage(i + 1, MessageUtil.getMessage(MessageConstant.Account.EMAIL_EXISTED));
                }
            }
            parseResult.setResult(insertResult);
        }
        return parseResult;
    }

    @SneakyThrows
    private ParseFileResult<CompanyEmployeeDTO> parseExcelFile(MultipartFile file, String companyId) {
        ParseFileResult<CompanyEmployeeDTO> parseResult;
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        if (workbook.getNumberOfSheets() != 1) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.XSL_NO_SHEET));
        }
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = xslsFileService.readXSLSheet(sheet, CompanyEmployeeConstant.XLSXFormat.COLUMN_NUM);
        parseResult = createNewEmployeeFromListString(data, companyId);

        if (parseResult.isHasError()) {
            String url = xslsFileService.uploadErrorXSLFile(workbook, parseResult.getErrors(), file.getOriginalFilename(), CompanyEmployeeConstant.XLSXFormat.ERROR_INDEX);
            parseResult.setErrorFileUrl(url);
        }
        return parseResult;
    }

    @SneakyThrows
    private ParseFileResult<CompanyEmployeeDTO> parseCsvFile(MultipartFile file, String companyId) {
        ParseFileResult<CompanyEmployeeDTO> parseResult;
        List<List<String>> data = xslsFileService.readCSVFile(file.getInputStream());
        parseResult = createNewEmployeeFromListString(data, companyId);
        if (parseResult.isHasError()) {
            String url = xslsFileService.uploadErrorCSVFile(data, parseResult.getErrors(), file.getOriginalFilename());
            parseResult.setErrorFileUrl(url);
        }
        return parseResult;
    }

    @Override
    @Transactional
    @SneakyThrows
    public ParseFileResult<CompanyEmployeeDTO> createNewCompanyEmployeesFromFile(MultipartFile file, String companyId) {
        //check for invalid type
        List<String> allowTypes = Arrays.asList(FileConstant.CSV_CONSTANT.TYPE, FileConstant.XLS_CONSTANT.TYPE, FileConstant.XLSX_CONSTANT.TYPE);
        String fileType = file.getContentType();
        if (!allowTypes.contains(fileType)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.File.NOT_ALLOWED));
        }
        ParseFileResult<CompanyEmployeeDTO> parseResult;
        if (Objects.equals(fileType, FileConstant.XLSX_CONSTANT.TYPE) || Objects.equals(fileType, FileConstant.XLS_CONSTANT.TYPE)) {
            parseResult = parseExcelFile(file, companyId);
            return parseResult;
        }
        parseResult = parseCsvFile(file, companyId);
        return parseResult;
    }


}
