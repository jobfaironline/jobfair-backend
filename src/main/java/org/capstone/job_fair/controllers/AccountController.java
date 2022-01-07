package org.capstone.job_fair.controllers;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    @GetMapping(ApiEndPoint.Account.ACCOUNT_ENDPOINT)
    public ResponseEntity<List<Account>> getAllAccounts(){
        return new ResponseEntity<List<Account>>(accountService.getAllAccounts(), HttpStatus.OK);
    }
}
