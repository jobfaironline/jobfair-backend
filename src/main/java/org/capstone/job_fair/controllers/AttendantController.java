package org.capstone.job_fair.controllers;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.services.attendant.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AttendantController {

    @Autowired
    private AttendantService attendantService;
    @GetMapping(ApiEndPoint.Account.ACCOUNT_ENDPOINT)
    public ResponseEntity<List<AttendantEntity>> getAllAccounts(){
        return new ResponseEntity<List<AttendantEntity>>(attendantService.getAllAccounts(), HttpStatus.OK);
    }
}
