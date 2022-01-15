package org.capstone.job_fair.controllers.company;


import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.controllers.payload.CompanyManagerRegisterRequest;
import org.capstone.job_fair.controllers.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.controllers.payload.UpdateCompanyEmployeeProfileRequest;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.account.GenderService;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyEmployeeController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private GenderService genderService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private CompanyService companyService;

    private boolean isGenderExist(int genderID) {
        return genderService.findById(genderID).isPresent();
    }

    private boolean isEmailExist(String email) {
        return accountService.getCountByActiveEmail(email) != 0;
    }


    @PostMapping(ApiEndPoint.CompanyEmployee.REGISTER_COMPANY_MANAGER)
    public ResponseEntity<?> register(@Validated @RequestBody CompanyManagerRegisterRequest request) {
        //check if email existed?
        if (isEmailExist(request.getEmail())) {
            return GenericMessageResponseEntity.build(
                    MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMAIL_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }
        //check if password match confirm password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return GenericMessageResponseEntity.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.CONFIRM_PASSWORD_MISMATCH),
                    HttpStatus.BAD_REQUEST);
        }
        //check gender validation
        if (!isGenderExist(request.getGender().ordinal())) {
            return GenericMessageResponseEntity.build(
                    MessageUtil.getMessage(MessageConstant.Gender.NOT_FOUND),
                    HttpStatus.BAD_REQUEST);
        }

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

        companyEmployeeService.createNewCompanyManagerAccount(dto);
        return GenericMessageResponseEntity.build(
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.CREATE_EMPLOYEE_MANAGER_SUCCESSFULLY),
                HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('COMPANY_EMPLOYEE') or hasAuthority('COMPANY_MANAGER')")
    @PostMapping(ApiEndPoint.CompanyEmployee.UPDATE_PROFILE_ENDPOINT)
    public ResponseEntity<?> updateProfile(@Validated @RequestBody UpdateCompanyEmployeeProfileRequest request) {

        if (!(accountService.getCountActiveAccountById(request.getAccountId()) == 0)) {
            return GenericMessageResponseEntity.build(
                    MessageConstant.Account.NOT_FOUND,
                    HttpStatus.BAD_REQUEST);
        }

        if (request.getAccountRequest() != null && request.getAccountRequest().getEmail() != null && isEmailExist(request.getAccountRequest().getEmail())) {
            return GenericMessageResponseEntity.build(
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
            if (!(companyService.getCountById(request.getCompanyId()) == 0)) {
                return GenericMessageResponseEntity.build(
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

        return GenericMessageResponseEntity.build(
                MessageUtil.getMessage(MessageConstant.CompanyEmployee.UPDATE_PROFILE_SUCCESSFULLY),
                HttpStatus.OK);
    }


}
