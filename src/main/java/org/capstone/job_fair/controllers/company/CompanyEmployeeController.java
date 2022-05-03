package org.capstone.job_fair.controllers.company;


import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.CompanyEmployeeConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CompanyEmployeeRegisterRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CompanyManagerRegisterRequest;
import org.capstone.job_fair.controllers.payload.requests.company.UpdateCompanyEmployeeProfileRequest;
import org.capstone.job_fair.controllers.payload.responses.CompanyEmployeeResponse;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class CompanyEmployeeController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MailService mailService;

    @Autowired
    private CompanyEmployeeMapper companyEmployeeMapper;

    @Autowired
    private CompanyMapper companyMapper;

    private boolean isEmailExist(String email) {
        return accountService.getCountAccountByEmail(email) != 0;
    }


    @Transactional
    @PostMapping(ApiEndPoint.CompanyEmployee.REGISTER_COMPANY_MANAGER)
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
        CompanyDTO companyDTO = companyService.getCompanyById(request.getCompanyId()).get();

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
        dto.setDepartment(request.getDepartment());

        dto = companyEmployeeService.createNewCompanyManagerAccount(dto);
        accountService.sendVerifyAccountEmail(dto.getAccount().getId());

        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.CREATE_EMPLOYEE_MANAGER_SUCCESSFULLY),
                HttpStatus.CREATED);

    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PutMapping(ApiEndPoint.CompanyEmployee.UPDATE_PROFILE_ENDPOINT)
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateCompanyEmployeeProfileRequest request) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ((accountService.getCountActiveAccountById(userDetails.getId()) == 0)) {
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
                        .id(userDetails.getId())
                        .email(request.getAccountRequest().getEmail())
                        .phone(request.getAccountRequest().getPhone())
                        .profileImageUrl(request.getAccountRequest().getProfileImageUrl())
                        .firstname(request.getAccountRequest().getFirstname())
                        .middlename(request.getAccountRequest().getMiddlename())
                        .lastname(request.getAccountRequest().getLastname())
                        .status(request.getAccountRequest().getStatus())
                        .gender(request.getAccountRequest().getGender()).build() :
                new AccountDTO();


        CompanyEmployeeDTO companyEmployeeDTO = CompanyEmployeeDTO.builder()
                .account(accountDTO)
                .accountId(userDetails.getId())
                .build();
        companyEmployeeService.updateProfile(companyEmployeeDTO);

        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.UPDATE_PROFILE_SUCCESSFULLY),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.CompanyEmployee.COMPANY_EMPLOYEE_ENDPOINT + "/{companyId}")
    public ResponseEntity<?> getCompanyEmployees(@PathVariable String companyId,
                                                 @RequestParam(value = "offset", defaultValue = CompanyEmployeeConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = CompanyEmployeeConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                                 @RequestParam(value = "sortBy", required = false, defaultValue = CompanyEmployeeConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                                 @RequestParam(value = "direction", required = false, defaultValue = CompanyEmployeeConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
                                                 @XSSConstraint @RequestParam(value = "searchContent", required = false, defaultValue = CompanyEmployeeConstant.DEFAULT_SEARCH_CONTENT) String searchContent) {
        Page<CompanyEmployeeResponse> employees = companyEmployeeService.getAllCompanyEmployees(companyId, searchContent, pageSize, offset, sortBy, direction).map(companyEmployeeMapper::toResponse);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @DeleteMapping(ApiEndPoint.CompanyEmployee.COMPANY_EMPLOYEE_ENDPOINT + "/{id}")
    public ResponseEntity<?> deleteCompanyEmp(@PathVariable String id) {
        Boolean result = companyEmployeeService.deleteEmployee(id);
        return result
                ? GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyEmployee.DELETE_SUCCESSFULLY), HttpStatus.OK)
                : GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyEmployee.DELETE_FAILED), HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @PostMapping(ApiEndPoint.CompanyEmployee.COMPANY_EMPLOYEE_ENDPOINT)
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<ResponseEntity<?>> createEmployee(@Validated @RequestBody CompanyEmployeeRegisterRequest request) {
        try {
            CompanyEmployeeDTO dto = companyEmployeeMapper.toDTO(request);
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String companyId = userDetails.getCompanyId();
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setId(companyId);
            dto.setCompanyDTO(companyDTO);
            companyEmployeeService.createNewCompanyEmployeeAccount(dto);

            this.mailService.sendMail(request.getEmail(),
                            MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_SUBJECT),
                            MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_CONTENT) + dto.getAccount().getPassword())
                    .exceptionally(throwable -> {
                        log.error(throwable.getMessage());
                        return null;
                    });
            return CompletableFuture.completedFuture(GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.CompanyEmployee.CREATE_EMPLOYEE_EMPLOYEE_SUCCESSFULLY),
                    HttpStatus.CREATED));
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(GenericResponse.build(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST));
        } catch (UnsupportedEncodingException | MessagingException ex2) {
            return CompletableFuture.completedFuture(GenericResponse.build(MessageUtil.getMessage(MessageConstant.Mail.SEND_FAILED), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.CompanyEmployee.PROMOTE_EMPLOYEE_ENDPOINT + "/{employeeId}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String employeeId) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            companyEmployeeService.promoteEmployee(employeeId, userDetails.getId());
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.CompanyEmployee.PROMOTE_EMPLOYEE_SUCCESSFULLY),
                    HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.CompanyEmployee.COMPANY_EMPLOYEE_ENDPOINT)
    public ResponseEntity<?> getEmployeeById(@RequestParam(value = "employeeID", required = true) String employeeID) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyID = null;
        //Get role from secutiry context
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        //If company manager then set company id
        if (role.equals(Role.COMPANY_MANAGER.getAuthority())) {
            companyID = userDetails.getCompanyId();
        }
        //If role is admin => company id will be null
        //Role is comany manager then company id will be their companyid
        CompanyEmployeeDTO employeeDTO = companyEmployeeService.getCompanyEmployeeByAccountId(employeeID, companyID);
        if (employeeDTO == null)
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND), HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(employeeDTO);
    }

    @PostMapping(ApiEndPoint.CompanyEmployee.UPLOAD_CSV_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> createMultipleCompanyEmployeeFromCSVFile(@RequestPart("file") MultipartFile file){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        List<CompanyEmployeeDTO> companyEmployeeDTOS = companyEmployeeService.createNewCompanyEmployeesFromCSVFile(file, companyId);
        return ResponseEntity.ok(companyEmployeeDTOS);
    }

}
