package org.capstone.job_fair.controllers.attendant;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.payload.AttendantRegisterRequest;
import org.capstone.job_fair.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class AttendantController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AttendantService attendantService;
    @PostMapping(ApiEndPoint.Attendant.REGISTER_ENDPOINT)
    public ResponseEntity<?> register(@Validated @RequestBody AttendantRegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new GenericMessageResponseEntity("Confirm password mismatch", HttpStatus.BAD_REQUEST);
        }
        Optional<AccountEntity> existedAccount = accountService.getActiveAccountByEmail(request.getEmail());
        if (existedAccount.isPresent()) {
            return new GenericMessageResponseEntity("Existed account", HttpStatus.BAD_REQUEST);
        }
        AccountDTO dto = new AccountDTO();
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setFirstname(request.getFirstName());
        dto.setLastname(request.getLastName());
        dto.setFirstname(request.getFirstName());
        dto.setGender(request.getGender());
        accountService.createNewAccount(dto);
        return new GenericMessageResponseEntity("Noice", HttpStatus.OK);
    }
    @PostMapping(ApiEndPoint.Attendant.UPDATE_ENDPOINT)
    public ResponseEntity<?> update(@RequestBody AttendantDTO req){
        HttpHeaders responseHeader = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeader).body(attendantService.save(req));
    }
    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT + "{email}")
    public ResponseEntity<?> getAttendant(@PathVariable("email")String email){
        System.out.println(email);
        HttpHeaders responseHeader = new HttpHeaders();
        return  ResponseEntity.status(HttpStatus.OK).headers(responseHeader).body(attendantService.getAttendantByEmail(email));
    }
}
