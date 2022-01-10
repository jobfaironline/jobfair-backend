package org.capstone.job_fair.controllers;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AttendantController {

    @Autowired
    private AccountService accountService;
    @GetMapping(ApiEndPoint.Attendant.ACCOUNT_ENDPOINT)
    public ResponseEntity<List<AccountEntity>> getAllAccounts(){
        return new ResponseEntity<List<AccountEntity>>(accountService.getAllAccounts(), HttpStatus.OK);
    }
}
