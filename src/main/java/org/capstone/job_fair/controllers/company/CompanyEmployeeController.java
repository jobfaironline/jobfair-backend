package org.capstone.job_fair.controllers.company;


import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.CompanyEmployeeRegisterRequest;
import org.capstone.job_fair.controllers.payload.requests.PromoteEmployeeToCompanyManagerRequest;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.controllers.payload.requests.CompanyManagerRegisterRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.controllers.payload.requests.UpdateCompanyEmployeeProfileRequest;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.account.GenderService;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.token.AccountVerifyTokenService;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.services.mappers.CompanyMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@NoArgsConstructor
public class CompanyEmployeeController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private GenderService genderService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MailService mailService;

    @Autowired
    private AccountVerifyTokenService accountVerifyTokenService;

    @Value("${api.endpoint}")
    String domain;

    @Autowired
    private CompanyMapper companyMapper;



    private boolean isEmailExist(String email) {
        return accountService.getCountAccountByEmail(email) != 0;
    }

    private boolean isCompanyExist(String companyId) {
        return companyService.getCountById(companyId) != 0;
    }

    @Transactional
    @PostMapping(ApiEndPoint.CompanyEmployee.REGISTER_COMPANY_MANAGER)
    @Async("threadPoolTaskExecutor")
    public ResponseEntity<?> register(@Validated @RequestBody CompanyManagerRegisterRequest request) {
        //check if email existed?
        if (isEmailExist(request.getEmail())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }
        //check if password match confirm password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.CONFIRM_PASSWORD_MISMATCH),
                    HttpStatus.BAD_REQUEST);
        }
        CompanyEntity companyEntity = companyService.getCompanyById(request.getCompanyId()).get();
        CompanyDTO companyDTO = companyMapper.toDTO(companyEntity);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(request.getEmail());
        accountDTO.setPassword(request.getPassword());
        accountDTO.setPhone(request.getPhone());
        accountDTO.setFirstname(request.getFirstName());
        accountDTO.setLastname(request.getLastName());
        accountDTO.setMiddlename(request.getMiddleName());
        accountDTO.setGender(request.getGender());

        CompanyEmployeeDTO dto = new CompanyEmployeeDTO();
        dto.setAccount(accountDTO);
        dto.setCompanyDTO(companyDTO);

        CompanyEmployeeDTO companyEmployeeDTO = companyEmployeeService.createNewCompanyManagerAccount(dto);
        String id = accountVerifyTokenService.createToken(companyEmployeeDTO.getAccount().getId()).getAccountId();
        this.mailService.sendMail(request.getEmail(),
                MessageUtil.getMessage(MessageConstant.Attendant.ACCOUNT_VERIFY_MAIL_TITLE),
                domain + ApiEndPoint.Authorization.VERIFY_USER+"/"+companyEmployeeDTO.getAccount().getId()+"/"+id);
        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.CREATE_EMPLOYEE_MANAGER_SUCCESSFULLY),
                HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PutMapping(ApiEndPoint.CompanyEmployee.UPDATE_PROFILE_ENDPOINT)
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateCompanyEmployeeProfileRequest request) {

        if ((accountService.getCountActiveAccountById(request.getAccountId()) == 0)) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND),
                    HttpStatus.BAD_REQUEST);
        }

        if (request.getAccountRequest() != null && request.getAccountRequest().getEmail() != null && isEmailExist(request.getAccountRequest().getEmail())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        AccountDTO accountDTO = request.getAccountRequest() != null ?
                AccountDTO.builder()
                        .id(request.getAccountId())
                        .email(request.getAccountRequest().getEmail())
                        .phone(request.getAccountRequest().getPhone())
                        .profileImageUrl(request.getAccountRequest().getProfileImageUrl())
                        .firstname(request.getAccountRequest().getFirstname())
                        .middlename(request.getAccountRequest().getMiddlename())
                        .lastname(request.getAccountRequest().getLastname())
                        .status(request.getAccountRequest().getStatus())
                        .gender(request.getAccountRequest().getGender()).build() :
                new AccountDTO();


        CompanyDTO companyDTO = null;
        if (request.getCompanyId() != null) {
            if ((companyService.getCountById(request.getCompanyId()) == 0)) {
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND)
                        , HttpStatus.BAD_REQUEST);
            }
            companyDTO = new CompanyDTO();
            companyDTO.setId(request.getCompanyId());
        }

        CompanyEmployeeDTO companyEmployeeDTO = CompanyEmployeeDTO.builder()
                .companyDTO(companyDTO)
                .account(accountDTO)
                .accountId(request.getAccountId())
                .build();
        companyEmployeeService.updateProfile(companyEmployeeDTO);

        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.UPDATE_PROFILE_SUCCESSFULLY),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.CompanyEmployee.COMPANY_EMPLOYEE_ENDPOINT + "/{companyId}")
    public ResponseEntity<?> getCompanyEmployees(@PathVariable String companyId) {
        List<CompanyEmployeeDTO> employees = companyEmployeeService.getAllCompanyEmployees(companyId);
        if(employees.isEmpty()) return new ResponseEntity<>(MessageUtil.getMessage(MessageConstant.Exception.RESOURCE_NOT_FOUND), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(employees ,HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @DeleteMapping(ApiEndPoint.CompanyEmployee.COMPANY_EMPLOYEE_ENDPOINT + "/{id}")
    public ResponseEntity<?> deleteCompanyEmp(@PathVariable String id){
        Boolean result = companyEmployeeService.deleteEmployee(id);
        return result
                ? GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyEmployee.DELETE_SUCCESSFULLY), HttpStatus.OK)
                : GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyEmployee.DELETE_FAILED), HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @PostMapping(ApiEndPoint.CompanyEmployee.COMPANY_EMPLOYEE_ENDPOINT)
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<ResponseEntity<?>> createEmployee(@Validated @RequestBody CompanyEmployeeRegisterRequest request){
        //check if email existed?
        if (isEmailExist(request.getEmail())) {
            return CompletableFuture.completedFuture(GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_EXISTED),
                    HttpStatus.BAD_REQUEST));
        }
        //check if company existed?
        if (!isCompanyExist(request.getCompanyId())) {
            return CompletableFuture.completedFuture(GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND),
                    HttpStatus.BAD_REQUEST));
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(request.getEmail());
        accountDTO.setPassword(PasswordGenerator.generatePassword());
        accountDTO.setPhone(request.getPhone());
        accountDTO.setFirstname(request.getFirstName());
        accountDTO.setLastname(request.getLastName());
        accountDTO.setMiddlename(request.getMiddleName());
        accountDTO.setGender(request.getGender());

        CompanyEmployeeDTO dto = new CompanyEmployeeDTO();
        dto.setAccount(accountDTO);

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(request.getCompanyId());
        dto.setCompanyDTO(companyDTO);

        companyEmployeeService.createNewCompanyEmployeeAccount(dto);
        this.mailService.sendMail(request.getEmail(),
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_SUBJECT),
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_CONTENT) + dto.getAccount().getPassword());
        return CompletableFuture.completedFuture(GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.CREATE_EMPLOYEE_EMPLOYEE_SUCCESSFULLY),
                HttpStatus.CREATED));
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.CompanyEmployee.PROMOTE_EMPLOYEE_ENPOINT)
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<ResponseEntity> promoteEmployee(@Validated @RequestBody PromoteEmployeeToCompanyManagerRequest request){
        CompletableFuture<ResponseEntity> response = null;
        try {
            companyEmployeeService.promoteEmployee(request.getEmployeeId(), request.getManagerId());
          response = CompletableFuture.completedFuture(GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.CompanyEmployee.PROMOTE_EMPLOYEE_SUCCESSFULLY),
                    HttpStatus.OK
            ));
        } catch (IllegalArgumentException ex){
             response = CompletableFuture.completedFuture(GenericResponse.build(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST
            ));
        } finally {
            return response;
        }
    }

}
