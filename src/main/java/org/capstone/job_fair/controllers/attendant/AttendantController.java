package org.capstone.job_fair.controllers.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.controllers.payload.AttendantRegisterRequest;
import org.capstone.job_fair.controllers.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AttendantController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AttendantService attendantService;

    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT)
    public ResponseEntity<List<AccountEntity>> getAllAccounts() {
        return new ResponseEntity<List<AccountEntity>>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    private AttendantDTO mappingRegisterRequestToDTO(AttendantRegisterRequest request) {
        AccountDTO dto = new AccountDTO();
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setFirstname(request.getFirstName());
        dto.setLastname(request.getLastName());
        dto.setFirstname(request.getFirstName());
        dto.setGender(request.getGender());

        AttendantDTO attendantDTO = new AttendantDTO();
        attendantDTO.setAccount(dto);
        attendantDTO.setAccountId(dto.getId());
        attendantDTO.setAccount(dto);
        return attendantDTO;
    }

    @PostMapping(ApiEndPoint.Attendant.REGISTER_ENDPOINT)
    public ResponseEntity<?> register(@Validated @RequestBody AttendantRegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return GenericMessageResponseEntity.build("Confirm password mismatch", HttpStatus.BAD_REQUEST);
        }
        Optional<AccountEntity> existedAccount = accountService.getActiveAccountByEmail(request.getEmail());
        if (existedAccount.isPresent()) {
            return GenericMessageResponseEntity.build("Existed account", HttpStatus.BAD_REQUEST);
        }

        AttendantDTO attendantDTO = mappingRegisterRequestToDTO(request);
        attendantService.createNewAccount(attendantDTO);
        return GenericMessageResponseEntity.build("Noice", HttpStatus.OK);
    }
    @PostMapping(ApiEndPoint.Attendant.UPDATE_ENDPOINT)
    public ResponseEntity<?> update(@RequestBody AttendantDTO req){
        HttpHeaders responseHeader = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeader).body(attendantService.save(req));
    }
    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT + "/{email}")
    public ResponseEntity<?> getAttendant(@PathVariable("email")String email){
        HttpHeaders responseHeader = new HttpHeaders();
        return  ResponseEntity.status(HttpStatus.OK).headers(responseHeader).body(attendantService.getAttendantByEmail(email));
    }
}
