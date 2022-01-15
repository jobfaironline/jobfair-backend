package org.capstone.job_fair.controllers.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;

import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.controllers.payload.AttendantRegisterRequest;
import org.capstone.job_fair.controllers.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    private AttendantDTO mappingRegisterRequestToDTO(AttendantRegisterRequest request) {
        AccountDTO dto = new AccountDTO();
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setFirstname(request.getFirstName());
        dto.setLastname(request.getLastName());
        dto.setMiddlename(request.getMiddleName());
        dto.setGender(request.getGender());

        AttendantDTO attendantDTO = new AttendantDTO();
        attendantDTO.setAccount(dto);
        attendantDTO.setAccountId(dto.getId());
        return attendantDTO;
    }

    @PostMapping(ApiEndPoint.Attendant.REGISTER_ENDPOINT)
    public ResponseEntity<?> register(@Validated @RequestBody AttendantRegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return GenericMessageResponseEntity.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.CONFIRM_PASSWORD_MISMATCH),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<AccountEntity> existedAccount = accountService.getActiveAccountByEmail(request.getEmail());
        if (existedAccount.isPresent()) {
            return GenericMessageResponseEntity.build(
                    MessageUtil.getMessage(MessageConstant.Account.EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        AttendantDTO attendantDTO = mappingRegisterRequestToDTO(request);
        attendantService.createNewAccount(attendantDTO);
        return GenericMessageResponseEntity.build(
                MessageUtil.getMessage(MessageConstant.Attendant.REGISTER_SUCCESSFULLY),
                HttpStatus.OK);
    }

    @PostMapping(ApiEndPoint.Attendant.UPDATE_ENDPOINT)
    public ResponseEntity<?> update(@Validated @RequestBody UpdateAttendantRequest req) {
        AccountDTO accountDTO = req.getAccount() != null ? AccountDTO.builder()
                .id(req.getAccountId())
                .lastname(req.getAccount().getLastname())
                .firstname(req.getAccount().getFirstname())
                .middlename(req.getAccount().getMiddlename())
                .email(req.getAccount().getEmail())
                .gender(req.getAccount().getGender())
                .phone(req.getAccount().getPhone())
                .profileImageUrl(req.getAccount().getProfileImageUrl())
                .build() : new AccountDTO();
        AttendantDTO dto = AttendantDTO.builder()
                .accountId(req.getAccountId())
                .account(accountDTO)
                .address(req.getAddress())
                .dob(req.getDob())
                .yearOfExp(req.getYearOfExp())
                .title(req.getTitle())
                .maritalStatus(req.getMaritalStatus())
                .build();

        if (accountService.getCountByActiveEmail(accountDTO.getEmail()) != 0) {
            return GenericMessageResponseEntity.build(
                    MessageUtil.getMessage(MessageConstant.Account.EMAIL_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        attendantService.update(dto);
        return GenericMessageResponseEntity.build(
                MessageUtil.getMessage(MessageConstant.Attendant.UPDATE_PROFILE_SUCCESSFULLY),
                HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT + "/{email}")
    public ResponseEntity<?> getAttendant(@PathVariable("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(attendantService.getAttendantByEmail(email));
    }
}
