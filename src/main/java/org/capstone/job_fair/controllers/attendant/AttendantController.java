package org.capstone.job_fair.controllers.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.payload.AttendantRegisterRequest;
import org.capstone.job_fair.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class AttendantController {

    @Autowired
    private AccountService accountService;

    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT)
    public ResponseEntity<List<AccountEntity>> getAllAccounts() {
        return new ResponseEntity<List<AccountEntity>>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @PostMapping(ApiEndPoint.Account.REGISTER_ENDPOINT)
    public ResponseEntity<?> register(@Validated @RequestBody AttendantRegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return  GenericMessageResponseEntity.build("Confirm password mismatch", HttpStatus.BAD_REQUEST);
        }
        Optional<AccountEntity> existedAccount = accountService.getActiveAccountByEmail(request.getEmail());
        if (existedAccount.isPresent()) {
            return  GenericMessageResponseEntity.build("Existed account", HttpStatus.BAD_REQUEST);
        }
        AccountDTO dto = new AccountDTO();
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setFirstname(request.getFirstName());
        dto.setLastname(request.getLastName());
        dto.setFirstname(request.getFirstName());
        dto.setGender(request.getGender());
        accountService.createNewAccount(dto);
        return GenericMessageResponseEntity.build("Noice", HttpStatus.OK);
    }
}
