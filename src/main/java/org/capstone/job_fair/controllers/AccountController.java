package org.capstone.job_fair.controllers;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.account.CreateAccountDTO;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.GenderEntity;
import org.capstone.job_fair.models.entities.attendant.RoleEntity;
import org.capstone.job_fair.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.payload.RegisterResponse;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.GenderService;
import org.capstone.job_fair.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AccountController {

    private static final String EMAIL_EXISTED = "Email has already existed. Please choose another one.";
    private static final String FAIL_TO_REGISTER = "Failed to register new account. Please check again.";
    private static final String SUCCESS_TO_REGISTER = "Register new account successfully.";
    private static final String CONFIRM_NOT_MATCH_PASSWORD = "Confirm password must match with password. Please check again.";
    private static final String GENDER_INVALID = "Chosen gender is invalid.";

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GenderService genderService;



    @PostMapping(ApiEndPoint.Account.REGISTER_COMPANY_MANAGER)
    public ResponseEntity<?> register(@RequestBody CreateAccountDTO dto) {
        //check if email existed?
        if (isEmailExist(dto.getEmail())) {
            return GenericMessageResponseEntity.build(EMAIL_EXISTED, HttpStatus.BAD_REQUEST);
        }
        //check if password match confirm password
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return GenericMessageResponseEntity.build(CONFIRM_NOT_MATCH_PASSWORD, HttpStatus.BAD_REQUEST);
        }
        //check gender validation
        if (!isGenderExist(dto.getGenderID())) {
            return GenericMessageResponseEntity.build(GENDER_INVALID, HttpStatus.BAD_REQUEST);
        }
        //map to entity
        AccountEntity account = accountService.convertCreateAccountDTOToEntity(dto);
        if (account == null) {
            return GenericMessageResponseEntity.build(FAIL_TO_REGISTER, HttpStatus.BAD_REQUEST);
        }
        accountService.save(account);
        return new ResponseEntity<>(new RegisterResponse(SUCCESS_TO_REGISTER), HttpStatus.CREATED);
    }


    //false: not exist, true: exist
    private boolean isGenderExist(int genderID) {
        return genderService.findById(genderID) != null ? true : false;
    }

    //true: exist, false: not exist
    private boolean isEmailExist(String email) {
        return accountService.getActiveAccountByEmail(email).isPresent() ? true : false;
    }
}

