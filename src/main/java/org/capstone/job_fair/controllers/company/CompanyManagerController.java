package org.capstone.job_fair.controllers.company;


import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.controllers.payload.CompanyManagerRegisterRequest;
import org.capstone.job_fair.controllers.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.controllers.payload.RegisterResponse;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.account.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyManagerController {
    private static final String EMAIL_EXISTED = "Email has already existed. Please choose another one.";
    private static final String FAIL_TO_REGISTER = "Failed to register new account. Please check again.";
    private static final String SUCCESS_TO_REGISTER = "Register new account successfully.";
    private static final String CONFIRM_NOT_MATCH_PASSWORD = "Confirm password must match with password. Please check again.";
    private static final String GENDER_INVALID = "Chosen gender is invalid.";

    @Autowired
    private AccountService accountService;

    @Autowired
    private GenderService genderService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;


    @PostMapping(ApiEndPoint.Account.REGISTER_COMPANY_MANAGER)
    public ResponseEntity<?> register(@Validated @RequestBody CompanyManagerRegisterRequest request) {
        //check if email existed?
        if (isEmailExist(request.getEmail())) {
            return GenericMessageResponseEntity.build(EMAIL_EXISTED, HttpStatus.BAD_REQUEST);
        }
        //check if password match confirm password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return GenericMessageResponseEntity.build(CONFIRM_NOT_MATCH_PASSWORD, HttpStatus.BAD_REQUEST);
        }
        //check gender validation
        if (!isGenderExist(request.getGender().ordinal())) {
            return GenericMessageResponseEntity.build(GENDER_INVALID, HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>(new RegisterResponse(SUCCESS_TO_REGISTER), HttpStatus.CREATED);
    }


    private boolean isGenderExist(int genderID) {
        return genderService.findById(genderID).isPresent();
    }

    private boolean isEmailExist(String email) {
        return accountService.getActiveAccountByEmail(email).isPresent();
    }
}
